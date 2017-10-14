package com.example.hanoc_000.countriesmaccabi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.example.hanoc_000.countriesmaccabi.R;
import com.example.hanoc_000.countriesmaccabi.model.Country;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Custom adapter for a countries list
 */
public class CountriesListAdapter extends BaseAdapter {

    private ArrayList<Country> countries;
    private Context context;
    private boolean shouldShowFlags;

//-------------------------------------------------------------------------------------------------

    private class ViewHolder {

        private String alpha3Code;

        private ImageView flag_iv;
        private TextView countryName_tv;
        private TextView countryNativeName_tv;
    }

//----------------------------------------------------------------------------------------------

    CountriesListAdapter(Context context, ArrayList<Country> countries, boolean shouldShowFlags) {

        this.shouldShowFlags = shouldShowFlags;
        this.countries = countries;
        this.context = context;
    }

//-------------------------------------------------------------------------------------------------

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Country getItem(int i) {
        return countries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

//----------------------------------------------------------------------------------------------

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        }

        Country country = getItem(i);

        String alpha3Code = country.alpha3Code;
        String name = country.name;
        String nativeName = country.nativeName;
        String flagUrl = country.flagUrl;

        ViewHolder holder = new ViewHolder();
        holder.alpha3Code = alpha3Code;
        holder.countryName_tv = (TextView) view.findViewById(R.id.countryName_tv);
        holder.countryNativeName_tv = (TextView) view.findViewById(R.id.countryNativeName_tv);
        holder.flag_iv = (ImageView) view.findViewById(R.id.flag_iv);

        holder.countryName_tv.setText(name);
        holder.countryNativeName_tv.setText(nativeName);

        view.setTag(holder);

        if (shouldShowFlags) {
            holder.flag_iv.setVisibility(View.INVISIBLE);

            if (flagUrl != null) {
                GetFlagFromUrl getFlagFromUrl = new GetFlagFromUrl(alpha3Code, flagUrl, holder);
                getFlagFromUrl.execute();
            }
        } else {
            holder.flag_iv.setVisibility(View.GONE);
        }

        return view;
    }

//----------------------------------------------------------------------------------------------

    /**
     * An asynchronous task for downloading a country's flag image, with
     * the URL given from RestCountries API.
     *
     * The downloaded image file is in .SVG format, therefore some manipulations should be made on it
     * before setting it into the ImageView
     */
    private class GetFlagFromUrl extends AsyncTask<Void, Void, Bitmap> {

        private String alpha3Code;
        private String flagUrl;
        private ViewHolder holder;

        GetFlagFromUrl(String alpha3Code, String flagUrl, ViewHolder holder) {

            this.alpha3Code = alpha3Code;
            this.flagUrl = flagUrl;
            this.holder = holder;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;

            try {
                final URL url = new URL(flagUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                // SVG manipulations:
                SVG svg = SVG.getFromInputStream(inputStream);
                if (svg.getDocumentWidth() != -1) {
                    bitmap = Bitmap.createBitmap((int) Math.ceil(svg.getDocumentWidth()),
                            (int) Math.ceil(svg.getDocumentHeight()),
                            Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(bitmap);

                    // Clear background to white
                    canvas.drawRGB(255, 255, 255);

                    // Render our document onto our canvas
                    svg.renderToCanvas(canvas);
                }

                return bitmap;
            } catch (Exception e) {
                Log.e("GetFlagFromUrl", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null && holder.alpha3Code.equals(alpha3Code)) {
                holder.flag_iv.setImageBitmap(bitmap);
                holder.flag_iv.setVisibility(View.VISIBLE);
            }
        }
    }
}
