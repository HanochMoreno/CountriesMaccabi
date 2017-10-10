package com.example.hanoc_000.countriesmaccabi.control;

import com.example.hanoc_000.countriesmaccabi.model.Country;

import java.util.ArrayList;

/**
 * A listener notifies the RestCountries API delivered a response, or when API error has been detected.
 */
public interface GetCountriesListListener {
    void onSuccess(ArrayList<Country> countriesList);

    void onError(Throwable t);
}
