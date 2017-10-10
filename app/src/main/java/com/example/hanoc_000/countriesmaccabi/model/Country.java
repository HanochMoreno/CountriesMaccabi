package com.example.hanoc_000.countriesmaccabi.model;

import com.google.gson.annotations.SerializedName;

/**
 * A country model containing the country's details, as received from RestCountries API.
 */
public class Country {

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_NATIVE_NAME = "nativeName";
    public static final String EXTRA_BORDERS = "borders";
    public static final String EXTRA_FLAG_URL = "flagUrl";

    /**
     *
     */
    public String alpha3Code;
    public String name;
    public String nativeName;

    @SerializedName("flag")
    public String flagUrl;

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
