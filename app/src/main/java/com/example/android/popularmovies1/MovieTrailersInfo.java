package com.example.android.popularmovies1;

import android.util.Log;

import java.util.ArrayList;

public class MovieTrailersInfo {
    public   String  Display_Name;
    public   String  Trailer_Url;

    public  MovieTrailersInfo(String display_Name, String trailer_Url)
    {
        Display_Name = display_Name;
        Trailer_Url = trailer_Url;
    }

    static String arrayToString(ArrayList<MovieTrailersInfo> trailers){
        String result = "";
        try {
            for (int i = 0; i < trailers.size(); i++) {
                result += trailers.get(i).Display_Name + "comma" + trailers.get(i).Trailer_Url;
                if (i < trailers.size() - 1) {
                    result += "tailersComma";
                }
            }
        }catch (NullPointerException e){
            return "";
        }
        return result;
    }

    static ArrayList<MovieTrailersInfo> stringToArray(String string){


        String[] elements = string.split("tailersComma");

        ArrayList<MovieTrailersInfo> res = new ArrayList<MovieTrailersInfo>();

        for (String element : elements) {
            try {
                String[] item = element.split("comma");
                res.add(new MovieTrailersInfo(item[0], item[1]));
            } catch (IndexOutOfBoundsException e) {

            }
        }

        return res;
    }
}
