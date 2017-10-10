package com.example.hanoc_000.countriesmaccabi.rest_countries_api;


import android.util.Log;

import com.example.hanoc_000.countriesmaccabi.control.GetAllCountriesListener;
import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.model.MyRetrofit;

import java.util.ArrayList;

import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ApisManager {
    private static ApisManager instance;
    private final String TAG = "ApisManager";

    private RestCountriesApi restCountriesApi;

    private ApisManager() {
        restCountriesApi = MyRetrofit.getRestCountriesApi();
    }

//-------------------------------------------------------------------------------------------------

    public static ApisManager getInstance() {
        if (instance == null) {
            instance = new ApisManager();
        }
        return instance;
    }

//-------------------------------------------------------------------------------------------------

    public void getAllCountriesList(String[] queryFilters, final GetAllCountriesListener listener) {

        Subscriber<ArrayList<Country>> subscriber = new Subscriber<ArrayList<Country>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "getAllCountriesList ERROR");
                listener.onError(t);
            }

            @Override
            public void onNext(ArrayList<Country> countriesList) {
                Log.d(TAG, "getAllCountriesList finished successfully");
                listener.onSuccess(countriesList);
            }
        };

        String queryFieldsFilters = "";

        // TODO tests

        if (queryFilters == null || queryFilters.length == 0) {

        } else {
            for (String queryFilter : queryFilters) {
                queryFieldsFilters += ";" + queryFilter;
            }

            // Removing the first ";" char
            queryFieldsFilters = queryFieldsFilters.substring(1);
        }

        Single<ArrayList<Country>> getAllCountriesList = restCountriesApi.getAllCountries(queryFieldsFilters);

        getAllCountriesList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

//-------------------------------------------------------------------------------------------------

}