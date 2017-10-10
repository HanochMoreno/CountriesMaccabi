package com.example.hanoc_000.countriesmaccabi.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.example.hanoc_000.countriesmaccabi.R;
import com.example.hanoc_000.countriesmaccabi.control.GetCountriesListListener;
import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.rest_countries_api.ApiManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class BordersCountryActivity extends AppCompatActivity {

    private String name;
    private String nativeName;
    private String flagUrl;

    private TextView countryName_tv;
    private TextView countryNativeName_tv;
    private TextView title_tv;
    private TextView noBorderCountries_tv;
    private ListView borders_lv;
    private ImageView flag_iv;
    private ProgressDialog progressDialog;
    private CountriesListAdapter adapter;

//-------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borders_country);

        countryName_tv = (TextView) findViewById(R.id.countryName_tv);
        countryNativeName_tv = (TextView) findViewById(R.id.countryNativeName_tv);
        noBorderCountries_tv = (TextView) findViewById(R.id.noBorderCountries_tv);
        title_tv = (TextView) findViewById(R.id.title_tv);
        borders_lv = (ListView) findViewById(R.id.borders_lv);
        flag_iv = (ImageView) findViewById(R.id.flag_iv);

        Intent callingIntent = getIntent();

        name = callingIntent.getStringExtra(Country.EXTRA_NAME);
        nativeName = callingIntent.getStringExtra(Country.EXTRA_NATIVE_NAME);
        String[] borders = callingIntent.getStringArrayExtra(Country.EXTRA_BORDERS);
        flagUrl = callingIntent.getStringExtra(Country.EXTRA_FLAG_URL);

        if (borders.length == 0) {
            updateUI(false);
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle(getString(R.string.loading_data));
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();

            String[] filters = {"name", "alpha3Code", "nativeName", "flag"};
            ApiManager.getInstance().getCountriesDetailsList(filters, borders,
                    new GetCountriesListListener() {
                        @Override
                        public void onSuccess(ArrayList<Country> countriesList) {
                            adapter = new CountriesListAdapter(BordersCountryActivity.this, countriesList, true);
                            updateUI(true);
                        }

                        @Override
                        public void onError(Throwable t) {
                            progressDialog.dismiss();
                            View mainView = findViewById(android.R.id.content);

                            Snackbar snack = Snackbar.make(mainView, R.string.error_getting_data, Snackbar.LENGTH_LONG);
                            ViewGroup group = (ViewGroup) snack.getView();
                            group.setBackgroundColor(Color.RED);
                            snack.show();
                        }
                    });
        }
    }

//-------------------------------------------------------------------------------------------------

    /**
     * Updates the screen with the data received from the calling activity and RestCountries API
     * @param hasBorders: determines whether to show a list of border countries, or a "no borders" message.
     */
    private void updateUI(boolean hasBorders) {
        countryName_tv.setText(name);
        countryNativeName_tv.setText(nativeName);

        if (flagUrl == null) {
            flag_iv.setVisibility(View.GONE);
        } else {
            GetFlagFromUrl getFlagFromUrl = new GetFlagFromUrl(flagUrl);
            getFlagFromUrl.execute();
        }

        if (hasBorders) {
            // Adding a gap (divider-like) at the top of the listView
            borders_lv.addHeaderView(new View(this), null, true);

            borders_lv.setVisibility(View.VISIBLE);
            borders_lv.setAdapter(adapter);

            title_tv.setText(name + " " + getString(R.string.has_borders_with_the_following_countries));
            progressDialog.dismiss();
        } else {
            title_tv.setVisibility(View.GONE);
            noBorderCountries_tv.setVisibility(View.VISIBLE);
        }
    }

//----------------------------------------------------------------------------------------------

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
