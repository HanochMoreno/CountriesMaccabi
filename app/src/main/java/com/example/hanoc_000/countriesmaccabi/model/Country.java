package com.example.hanoc_000.countriesmaccabi.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Hanoc_000 on 10/10/2017.
 */

public class Country {

    public String alpha3Code;
    public String name;
    public String nativeName;

    @SerializedName("flag")
    public String flagUrl;

    public ArrayList<String> borders;
}

/*  EXAMPLE:

    {
       "flag": "https://restcountries.eu/data/afg.svg",
       "name": "Afghanistan",
       "alpha3Code": "AFG",
       "borders": ["IRN", "PAK", "TKM", "UZB", "TJK", "CHN"],
       "nativeName": "افغانستان"
    }

*/
