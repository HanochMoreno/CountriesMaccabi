package com.example.hanoc_000.countriesmaccabi.rest_countries_api;


import android.util.Log;

import com.example.hanoc_000.countriesmaccabi.control.GetCountriesListListener;
import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.model.MyRetrofit;

import java.util.ArrayList;

import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ApiManager {
    private static ApiManager instance;
    private final String TAG = "ApisManager";

    private RestCountriesApi restCountriesApi;

    private ApiManager() {
        restCountriesApi = MyRetrofit.getRestCountriesApi();
    }

//-------------------------------------------------------------------------------------------------

    public static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

//-------------------------------------------------------------------------------------------------

    public void getAllCountriesList(String[] queryFilters, final GetCountriesListListener listener) {

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

        String fieldsFiltersQuery = getStringFieldsQuery(queryFilters);

        Single<ArrayList<Country>> getAllCountriesList =
                restCountriesApi.getAllCountries(fieldsFiltersQuery);

        getAllCountriesList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

//-------------------------------------------------------------------------------------------------

    public void getCountriesDetailsList(String[] queryFilters, String[] countriesCodes
            , final GetCountriesListListener listener) {

        Subscriber<ArrayList<Country>> subscriber = new Subscriber<ArrayList<Country>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "getCountriesDetailsList ERROR");
                listener.onError(t);
            }

            @Override
            public void onNext(ArrayList<Country> countriesList) {
                Log.d(TAG, "getCountriesDetailsList finished successfully");
                listener.onSuccess(countriesList);
            }
        };

        String fieldsFiltersQuery = getStringFieldsQuery(queryFilters);
        String countriesCodesQuery = getCountriesCodesQuery(countriesCodes);

        Single<ArrayList<Country>> getAllCountriesList =
                restCountriesApi.getCountriesDetails(fieldsFiltersQuery, countriesCodesQuery);

        getAllCountriesList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

//-------------------------------------------------------------------------------------------------

    private String getStringFieldsQuery(String[] queryFilters) {
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

        return queryFieldsFilters;
    }
//-------------------------------------------------------------------------------------------------

    private String getCountriesCodesQuery(String[] countriesCodes) {
        String countriesCodesQuery = "";

        // TODO tests

        if (countriesCodes == null || countriesCodes.length == 0) {

        } else {
            for (String countryCode : countriesCodes) {
                countriesCodesQuery += ";" + countryCode;
            }

            // Removing the first ";" char
            countriesCodesQuery = countriesCodesQuery.substring(1);
        }

        return countriesCodesQuery;
    }

//-------------------------------------------------------------------------------------------------

}