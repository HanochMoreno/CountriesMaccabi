package com.example.hanoc_000.countriesmaccabi.model;


import com.example.hanoc_000.countriesmaccabi.rest_countries_api.RestCountriesApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A wrapper to Retrofit, using custom connection properties.
 */
public class MyRetrofit {

    public static final String REST_COUNTRIES_BASE_URL = "https://restcountries.eu/rest/v2/";

    private static Retrofit instance;
    private static RestCountriesApi restCountriesApi;

    private MyRetrofit(){}

//-------------------------------------------------------------------------------------------------

    public static Retrofit getInstance() {

        if (instance == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            instance = new Retrofit.Builder()
                    .baseUrl(REST_COUNTRIES_BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return instance;
    }

//-------------------------------------------------------------------------------------------------

    public static RestCountriesApi getRestCountriesApi() {
        if (restCountriesApi == null) {
            restCountriesApi = getInstance().create(RestCountriesApi.class);
        }
        return restCountriesApi;
    }
}