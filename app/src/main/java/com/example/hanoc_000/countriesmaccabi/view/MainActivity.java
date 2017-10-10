package com.example.hanoc_000.countriesmaccabi.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hanoc_000.countriesmaccabi.R;
import com.example.hanoc_000.countriesmaccabi.control.GetCountriesListListener;
import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.rest_countries_api.ApiManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CountriesListAdapter adapter;
    private ListView countries_lv;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countries_lv = (ListView) findViewById(R.id.countriesList);

        countries_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(MainActivity.this, BordersCountryActivity.class);
                intent.putExtra(Country.EXTRA_NAME, country.name);
                intent.putExtra(Country.EXTRA_NATIVE_NAME, country.nativeName);
                intent.putExtra(Country.EXTRA_BORDERS, country.borders);
                intent.putExtra(Country.EXTRA_FLAG_URL, country.flagUrl);
                startActivity(intent);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getString(R.string.loading_data));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        String[] filters = {"name", "alpha3Code", "borders", "nativeName", "flag"};
        ApiManager.getInstance().getAllCountriesList(filters,
                new GetCountriesListListener() {
                    @Override
                    public void onSuccess(ArrayList<Country> countriesList) {
                        adapter = new CountriesListAdapter(MainActivity.this, countriesList, false);
                        countries_lv.setAdapter(adapter);
                        progressDialog.dismiss();
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