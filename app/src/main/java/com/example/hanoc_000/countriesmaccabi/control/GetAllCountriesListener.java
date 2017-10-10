package com.example.hanoc_000.countriesmaccabi.control;

import com.example.hanoc_000.countriesmaccabi.model.Country;

import java.util.ArrayList;

/**
 * Created by Hanoc_000 on 10/10/2017.
 */

public interface GetAllCountriesListener {
    void onSuccess(ArrayList<Country> countriesList);

    void onError(Throwable t);
}
