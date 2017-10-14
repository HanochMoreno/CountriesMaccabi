package com.example.hanoc_000.countriesmaccabi.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.example.hanoc_000.countriesmaccabi.R;
import com.example.hanoc_000.countriesmaccabi.control.MainPresenter;
import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A screen presenting a list of the selected country's border countries (countries that have
 * common border with the selected country).
 */
public class CountryBordersActivity extends MyActivity {

    /**
     * The name of the country in English.
     */
    private String name;

    /**
     * The name of the country in the country native language.
     */
    private String nativeName;

    /**
     * The URL for downloading an image of the country's flag.
     */
    private String flagUrl;

    /**
     * A list of ISO 3166-1 country codes.
     * Represents the countries that have common border with this border.
     */
    public String[] borders;

    /**
     * The list of border countries, as received from the API response.
     * Used for avoiding another API call in case of activity recreation (screen rotation etc.)
     */
    private ArrayList<Country> countries;

    // Views
    private TextView countryName_tv;
    private TextView countryNativeName_tv;
    private TextView title_tv;
    private TextView noBorderCountries_tv;
    private ImageView flag_iv;


//-------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_borders_country);
        super.onCreate(savedInstanceState);

        countryName_tv = (TextView) findViewById(R.id.countryName_tv);
        countryNativeName_tv = (TextView) findViewById(R.id.countryNativeName_tv);
        noBorderCountries_tv = (TextView) findViewById(R.id.noBorderCountries_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        flag_iv = (ImageView) findViewById(R.id.flag_iv);

        // Getting the data of the selected country from the calling activity:
        Intent callingIntent = getIntent();
        name = callingIntent.getStringExtra(Country.EXTRA_NAME);
        nativeName = callingIntent.getStringExtra(Country.EXTRA_NATIVE_NAME);
        borders = callingIntent.getStringArrayExtra(Country.EXTRA_BORDERS);
        flagUrl = callingIntent.getStringExtra(Country.EXTRA_FLAG_URL);

        if (borders.length == 0) {
            // This country has no border countries- Notify the user about that.
            updateUI(null);
        } else if (savedInstanceState == null) {
            // Get the border countries relevant data from the RestCountries API.
            mainPresenter = new MainPresenter(this);
            mainPresenter.subscribe();
        }
    }

//-------------------------------------------------------------------------------------------------

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String countriesAsJson = savedInstanceState.getString("countries");
        Type jsonType = new TypeToken<ArrayList<Country>>() {}.getType();

        countries = new Gson().fromJson(countriesAsJson, jsonType);
        updateUI(countries);
    }

//-------------------------------------------------------------------------------------------------

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 'countries' saved locally in the activity state to avoid
        // another API call in case of activity recreation (screen rotation etc.)

        if (countries != null) {
            Type jsonType = new TypeToken<ArrayList<Country>>() {}.getType();

            outState.putString("countries", new Gson().toJson(countries, jsonType));
        }
    }

//-------------------------------------------------------------------------------------------------

    /**
     * Updates the screen with the data received from RestCountries API (if exists) and/or the
     * calling activity.

     * @param countries: The border countries list. If null a "no borders" message will appear.
     */
    private void updateUI(ArrayList<Country> countries) {
        countryName_tv.setText(name);
        countryNativeName_tv.setText(nativeName);

        if (flagUrl == null) {
            flag_iv.setVisibility(View.GONE);
        } else {
            GetFlagFromUrl getFlagFromUrl = new GetFlagFromUrl(flagUrl);
            getFlagFromUrl.execute();
        }

        if (countries == null) {
            title_tv.setVisibility(View.GONE);
            noBorderCountries_tv.setVisibility(View.VISIBLE);
        } else {
            // Adding a gap (divider-like) at the top of the listView
            countries_lv.addHeaderView(new View(this), null, true);
            countries_lv.setVisibility(View.VISIBLE);

            adapter = new CountriesListAdapter(this, countries, true);
            countries_lv.setAdapter(adapter);

            title_tv.setText(name + " " + getString(R.string.has_borders_with_the_following_countries));

            // Adding a delay of 2.5 secs before dismissing the progressDialog, giving the adapter
            // enough time to download and display the countries flags.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 2500);
        }
    }

//-------------------------------------------------------------------------------------------------

    /**
     * Called when the border countries' data received from RestCountries API.
     */
    @Override
    public void onFetchDataSuccess(ArrayList<Country> countries) {
        this.countries = countries;
        updateUI(countries);
    }

//-------------------------------------------------------------------------------------------------

    /**
     * An asynchronous task for downloading and presenting the country's flag.
     *
     * The downloaded image file is in .SVG format, therefore some manipulations should be made on it
     * before setting it into the ImageView
     */
    private class GetFlagFromUrl extends AsyncTask<Void, Void, Bitmap> {

        private String flagUrl;

        GetFlagFromUrl(String flagUrl) {

            this.flagUrl = flagUrl;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;

            try {
                final URL url = new URL(flagUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                SVG svg = SVG.getFromInputStream(inputStream);
                if (svg.getDocumentWidth() != -1) {
                    bitmap = Bitmap.createBitmap((int) Math.ceil(svg.getDocumentWidth()),
                            (int) Math.ceil(svg.getDocumentHeight()),
                            Bitmap.Config.ARGB_8888);

                    Canvas bmcanvas = new Canvas(bitmap);

                    // Clear background to white
                    bmcanvas.drawRGB(255, 255, 255);

                    // Render our document onto our canvas
                    svg.renderToCanvas(bmcanvas);
                }

                return bitmap;
            } catch (Exception e) {
                Log.e("GetFlagFromUrl", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {

                flag_iv.setImageBitmap(bitmap);
            }
        }
    }

}
