package com.example.hanoc_000.countriesmaccabi.rest_countries_api;

import android.support.annotation.NonNull;

/**
 * Created by Hanoc_000 on 13/10/2017.
 */
public abstract class ApiUtil {

    public static final String[] BORDER_COUNTRIES_ACTIVITY_REQUIRED_FIELDS
            = new String[]{"name", "alpha3Code", "nativeName", "flag"};

    public static final String[] MAIN_ACTIVITY_REQUIRED_FIELDS
            = new String[]{"name", "alpha3Code", "nativeName", "flag", "borders"};

//-------------------------------------------------------------------------------------------------

    /**
     * @param queryList: Could be a list of desired country fields, or a list of ISO 3166-1 country codes
     * @return A query string, formed as requested by RestCountries APi
     * <p>
     * Example1: ["borders", "nativeName", "flag"] --> borders;nativeName;flag .
     * Example2: ["ISR", "SOL", "BRA"] --> ISR;SOL;BRA .
     */
    public static String getStringQuery(@NonNull String[] queryList) {
        String queryFieldsFilters = "";

        for (String queryFilter : queryList) {
            queryFieldsFilters += ";" + queryFilter;
        }

        // Removing the first ";" char
        queryFieldsFilters = queryFieldsFilters.substring(1);

        return queryFieldsFilters;
    }
}
