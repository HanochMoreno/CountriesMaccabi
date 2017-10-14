package com.example.hanoc_000.countriesmaccabi.view;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hanoc_000.countriesmaccabi.R;
import com.example.hanoc_000.countriesmaccabi.control.MainContract;
import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.control.MainPresenter;

import java.util.ArrayList;

public abstract class MyActivity extends AppCompatActivity implements MainContract.View {

    protected ListView countries_lv;
    protected ProgressDialog progressDialog;
    protected CountriesListAdapter adapter;
    protected MainPresenter mainPresenter;

//-------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        countries_lv = (ListView) findViewById(R.id.countries_lv);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getString(R.string.loading_data));
        progressDialog.setMessage(getString(R.string.please_wait));
    }

//-------------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mainPresenter != null) {
            mainPresenter.unSubscribe();
            mainPresenter.onDestroy();
        }
    }

//-------------------------------------------------------------------------------------------------

    @Override
    public void onFetchDataStarted() {
        progressDialog.show();
    }

//-------------------------------------------------------------------------------------------------

    @Override
    public void onFetchDataCompleted() {
    }

//-------------------------------------------------------------------------------------------------

    @Override
    public abstract void onFetchDataSuccess(ArrayList<Country> countries);

//-------------------------------------------------------------------------------------------------

    @Override
    public void onFetchDataError(Throwable e) {
        Log.e(getClass().getName(), "getCountriesDetailsList ERROR");

        progressDialog.dismiss();
        View mainView = findViewById(android.R.id.content);

        Snackbar snack = Snackbar.make(mainView, R.string.error_getting_data, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(Color.RED);
        snack.show();
    }

}
