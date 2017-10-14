package com.example.hanoc_000.countriesmaccabi.rest_countries_api;

import com.example.hanoc_000.countriesmaccabi.model.Country;
import com.example.hanoc_000.countriesmaccabi.model.MyRetrofit;

import java.util.ArrayList;

import retrofit2.http.Query;
import rx.Single;

/**
 * A wrapper to RestCountriesApi.
 */
public class RestCountriesRemoteApi implements RestCountriesApi {

    private RestCountriesApi api;

    public RestCountriesRemoteApi() {
        api = MyRetrofit.getInstance().create(RestCountriesApi.class);
    }

    @Override
    public Single<ArrayList<Country>> getAllCountries(@Query("fields") String fields) {

        return api.getAllCountries(fields);
    }

    @Override
    public Single<ArrayList<Country>> getCountriesDetails(@Query("fields") String fields, @Query("codes") String countriesCodes) {

        return api.getCountriesDetails(fields, countriesCodes);
    }
}
