package com.example.hanoc_000.countriesmaccabi.model;

import com.google.gson.annotations.SerializedName;

/**
 * A country model containing the country's details, as received from RestCountries API.
 */
public class Country {

    /****  Constants for passing extras between the activities  ****/
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_NATIVE_NAME = "nativeName";
    public static final String EXTRA_BORDERS = "borders";
    public static final String EXTRA_FLAG_URL = "flagUrl";

    /**
     * ISO 3166-1 3-letters country code
     */
    public String alpha3Code;

    /**
     * The country name in English
     */
    public String name;

    /**
     * The country name in the country native language
     */
    public String nativeName;

    /**
     * A URL for getting the country flag (in .SVG format)
     */
    @SerializedName("flag")
    public String flagUrl;

    /**
     * A list of countries that have common borders with this country.
     * Represented in ISO 3166-1 3-letters country code.
     */
    public String[] borders;
}

/*  RESPONSE EXAMPLE:

    {
       "flag": "https://restcountries.eu/data/afg.svg",
       "name": "Afghanistan",
       "alpha3Code": "AFG",
       "borders": ["IRN", "PAK", "TKM", "UZB", "TJK", "CHN"],
       "nativeName": "افغانستان"
    }

*/
