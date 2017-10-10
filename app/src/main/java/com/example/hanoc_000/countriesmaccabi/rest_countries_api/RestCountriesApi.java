package com.example.hanoc_000.countriesmaccabi.rest_countries_api;

import com.example.hanoc_000.countriesmaccabi.AppConsts;
import com.example.hanoc_000.countriesmaccabi.model.Country;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created by Hanoc_000 on 10/10/2017.
 */

public interface RestCountriesApi {

    @GET(AppConsts.REST_COUNTRIES_BASE_URL + "all")
    Single<ArrayList<Country>> getAllCountries(@Query("fields") String fields);
}
