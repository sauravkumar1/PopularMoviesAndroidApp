package com.example.android.popularmovies1;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class MovieReviewsInfo {

public  String AuthorName;
public  String ReviewDetail;


    public  MovieReviewsInfo(String authorName, String reviewDetail)
    {
        AuthorName = authorName;
        ReviewDetail = reviewDetail;
    }
    static String arrayToString(ArrayList<MovieReviewsInfo> reviews){
        String result = "";
        try {
            for (int i = 0; i < reviews.size(); i++) {
                result += reviews.get(i).AuthorName + "commma"+ reviews.get(i).ReviewDetail;
                if (i < reviews.size() - 1) {
                    result += "reviewsComma";
                }
            }
        }catch (NullPointerException e){
            return "";
        }
        return result;
    }

    static ArrayList<MovieReviewsInfo> stringToArray(String string){
        String[] elements = string.split("reviewsComma");
        ArrayList<MovieReviewsInfo> res = new ArrayList<>();

        for (String element : elements) {
            String[] item = element.split("comma");
            try{
                res.add(new MovieReviewsInfo(item[0], item[1]));
            }catch (IndexOutOfBoundsException e){
                Log.d("REVIEWS",e.toString());
            }
        }
        return res;
    }
}
