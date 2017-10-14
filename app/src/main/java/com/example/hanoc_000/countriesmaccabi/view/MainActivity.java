package com.example.hanoc_000.countriesmaccabi.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.hanoc_000.countriesmaccabi.R;
import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.control.MainPresenter;

import java.util.ArrayList;

/**
 * An screen presenting a list of the all countries, as received from RestCountries APi.
 */
public class MainActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        countries_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(MainActivity.this, CountryBordersActivity.class);
                intent.putExtra(Country.EXTRA_NAME, country.name);
                intent.putExtra(Country.EXTRA_NATIVE_NAME, country.nativeName);
                intent.putExtra(Country.EXTRA_BORDERS, country.borders);
                intent.putExtra(Country.EXTRA_FLAG_URL, country.flagUrl);
                startActivity(intent);
            }
        });

        mainPresenter = new MainPresenter(this);
        mainPresenter.subscribe();
    }

//-------------------------------------------------------------------------------------------------

    @Override
    public void onFetchDataSuccess(ArrayList<Country> countries) {
        adapter = new CountriesListAdapter(MainActivity.this, countries, false);
        countries_lv.setAdapter(adapter);
        progressDialog.dismiss();
    }

}