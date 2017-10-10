package com.example.hanoc_000.countriesmaccabi.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hanoc_000.countriesmaccabi.R;
import com.example.hanoc_000.countriesmaccabi.control.GetAllCountriesListener;
import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.rest_countries_api.ApisManager;

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
                Country item = (Country) adapterView.getAdapter().getItem(i);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(getString(R.string.loading_data));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        String[] filters = {"name", "alpha3Code", "borders", "nativeName", "flag"};
        ApisManager.getInstance().getAllCountriesList(filters,
                new GetAllCountriesListener() {
                    @Override
                    public void onSuccess(ArrayList<Country> countriesList) {
                        adapter = new CountriesListAdapter(MainActivity.this, countriesList);
                        countries_lv.setAdapter(adapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable t) {

                        // TODO: STRING RESOURCES

                        Toast.makeText(MainActivity.this, R.string.error_getting_data, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
