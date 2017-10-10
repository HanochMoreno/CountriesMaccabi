package com.example.hanoc_000.countriesmaccabi.rest_countries_api;


import android.support.annotation.NonNull;
import android.util.Log;

import com.example.hanoc_000.countriesmaccabi.control.GetCountriesListListener;
import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.model.MyRetrofit;

import java.util.ArrayList;

import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A singleton type manager handling all API calls
 */
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

    /**
     * Gets a list of all countries from RestCountries API.
     *
     * @param queryFilters: A list of desired fields of each country, that will be received with the response
     * @param listener: A listener that notifies about the API response, or error if detected
     */
    public void getAllCountriesList(@NonNull String[] queryFilters, final GetCountriesListListener listener) {

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

        String fieldsFiltersQuery = getStringQuery(queryFilters);

        Single<ArrayList<Country>> getAllCountriesList =
                restCountriesApi.getAllCountries(fieldsFiltersQuery);

        getAllCountriesList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

//-------------------------------------------------------------------------------------------------

    /**
     * Gets the details of specific countries from RestCountries API.
     *
     * @param queryFilters: A list of desired fields of each country, that will be received with the response
     * @param countriesCodes: A list of ISO 3166-1 country codes Represent the requested countries
     * @param listener: A listener that notifies about the API response
     */
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

        String fieldsFiltersQuery = getStringQuery(queryFilters);
        String countriesCodesQuery = getStringQuery(countriesCodes);

        Single<ArrayList<Country>> getAllCountriesList =
                restCountriesApi.getCountriesDetails(fieldsFiltersQuery, countriesCodesQuery);

        getAllCountriesList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

//-------------------------------------------------------------------------------------------------

    /**
     * @param queryList: Could be a list of desired country fields, or a list of ISO 3166-1 country codes
     * @return A query string, as requested by RestCountries APi
     *
     * Example1: ["borders", "nativeName", "flag"] --> borders;nativeName;flag .
     * Example2: ["ISR", "SOL", "BRA"] --> ISR;SOL;BRA .
     */
    private String getStringQuery(@NonNull String[] queryList) {
        String queryFieldsFilters = "";

        for (String queryFilter : queryList) {
            queryFieldsFilters += ";" + queryFilter;
        }

        // Removing the first ";" char
        queryFieldsFilters = queryFieldsFilters.substring(1);

        return queryFieldsFilters;
    }

//-------------------------------------------------------------------------------------------------

}