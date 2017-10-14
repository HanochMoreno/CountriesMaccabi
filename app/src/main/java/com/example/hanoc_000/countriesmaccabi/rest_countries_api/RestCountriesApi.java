package com.example.hanoc_000.countriesmaccabi.rest_countries_api;

import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.model.MyRetrofit;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * Interface for creating a connection to RestCountries API with Retrofit.
 */
public interface RestCountriesApi {

    @GET(MyRetrofit.REST_COUNTRIES_BASE_URL + "all")
    Single<ArrayList<Country>> getAllCountries(@Query("fields") String fields);

    @GET(MyRetrofit.REST_COUNTRIES_BASE_URL + "alpha")
    Single<ArrayList<Country>> getCountriesDetails(@Query("fields") String fields, @Query("codes") String codes);
}
