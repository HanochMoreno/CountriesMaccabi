package com.example.hanoc_000.countriesmaccabi.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hanoc_000.countriesmaccabi.R;
import com.example.hanoc_000.countriesmaccabi.model.Country;
//import com.larvalabs.svgandroid.SVG;
//import com.larvalabs.svgandroid.SVGParser;
//import com.squareup.picasso.Picasso;
//
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
import java.util.ArrayList;

/**
 * Custom adapter for a recipes list
 */
public class CountriesListAdapter extends BaseAdapter {

    private ArrayList<Country> countries;
    private Context context;

//-------------------------------------------------------------------------------------------------

    public class ViewHolder {

//        private String alpha3Code;
//        private FrameLayout layout_listItemContainer;

//        private ImageView flag_iv;
        private TextView countryName_tv;
        private TextView countryNativeName_tv;
    }

//----------------------------------------------------------------------------------------------

    public CountriesListAdapter(Context context, ArrayList<Country> countries) {

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
//        String flagUrl = country.flagUrl;

        ViewHolder holder = new ViewHolder();
//        holder.alpha3Code = alpha3Code;
//        holder.layout_listItemContainer = (FrameLayout) view.findViewById(R.id.layout_listItemContainer);
        holder.countryName_tv = (TextView) view.findViewById(R.id.countryName_tv);
        holder.countryNativeName_tv = (TextView) view.findViewById(R.id.countryNativeName_tv);
//        holder.flag_iv = (ImageView) view.findViewById(R.id.flag_iv);

        holder.countryName_tv.setText(name);
        holder.countryNativeName_tv.setText(nativeName);
//        holder.flag_iv.setVisibility(View.INVISIBLE);

        view.setTag(holder);

//        Picasso.with(context).load(country.flagUrl).into(holder.flag_iv);

//        GetFlagFromUrl getImageFromSdCard = new GetFlagFromUrl(alpha3Code, flagUrl, holder);
//        getImageFromSdCard.execute();

        return view;
    }

//----------------------------------------------------------------------------------------------

//    private class GetFlagFromUrl extends AsyncTask<Void, Void, Drawable> {
//
//        private String flagUrl;
//        private String alpha3Code;
//        private ViewHolder holder;
//
//        GetFlagFromUrl(String alpha3Code, String flagUrl, ViewHolder holder) {
//
//            this.flagUrl = flagUrl;
//            this.alpha3Code = alpha3Code;
//            this.holder = holder;
//        }
//
//        @Override
//        protected Drawable doInBackground(Void... params) {
//            try {
//                final URL url = new URL(flagUrl);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = urlConnection.getInputStream();
//                SVG svg = SVGParser. getSVGFromInputStream(inputStream);
//                PictureDrawable drawable = svg.createPictureDrawable();
//                return drawable;
//            } catch (Exception e) {
//                Log.e("HttpImageRequestTask", e.getMessage(), e);
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Drawable drawable) {
//            // Update the view
//            if(drawable != null){
//
//                if (holder.alpha3Code.equals(alpha3Code)) {
//                    // Try using your library and adding this layer type before switching your SVG parsing
//                    holder.flag_iv.setImageDrawable(drawable);
//                }
//            }
//        }
//    }

//----------------------------------------------------------------------------------------------

//    private class GetFlagFromUrl extends AsyncTask<String, Integer, Bitmap> {
//
//        private String alpha3Code;
//        private ViewHolder holder;
//
//        public GetFlagFromUrl(String alpha3Code, ViewHolder holder) {
//
//            this.alpha3Code = alpha3Code;
//            this.holder = holder;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//
//            return ImageStorage.getImageBitmapByName(context, imageName);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap image) {
//
//            if (holder.alpha3Code.equals(alpha3Code)) {
//                holder.flag_iv.setImageBitmap(image);
//                holder.flag_iv.setVisibility(View.VISIBLE);
//            }
//        }
//    }

}
