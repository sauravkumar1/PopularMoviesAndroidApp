package com.example.android.popularmovies1.utils;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies1.MovieInfo;
import com.example.android.popularmovies1.MovieReviewsInfo;
import com.example.android.popularmovies1.MovieTrailersInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MovieJsonUtils {

    public static MovieInfo[] getSimpleMovieStringsFromJson(Context context, String movieJsonString,String posterUrl)
            throws JSONException {

        final String ID = "id";
        final String LIST = "results";
        final String RATING = "vote_average";
        final String POSTER_PATH = "poster_path";
        final String TITLE = "title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        MovieInfo[] parsedMovieData = null;
        JSONObject movieJson = new JSONObject(movieJsonString);
        JSONArray movieArray = movieJson.getJSONArray(LIST);

        parsedMovieData = new MovieInfo[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieObject = movieArray.getJSONObject(i);


            MovieInfo mI = new MovieInfo(movieObject.getString(ID), movieObject.getString(TITLE),
                    movieObject.getString(OVERVIEW),
                    movieObject.getString(RELEASE_DATE).substring(0,4),
                    movieObject.getString(RATING),
                    posterUrl.concat(movieObject.getString(POSTER_PATH)), null);

                    parsedMovieData[i] = mI;
        }
        return parsedMovieData;

    }



    public   static  ArrayList<MovieTrailersInfo> GetTrailersFromJsonStrig(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        JSONArray trailers = json.getJSONArray("results");
        ArrayList<MovieTrailersInfo>  result = new ArrayList<MovieTrailersInfo>();

        try{
        for (int i = 0; i< trailers.length(); i++){
            JSONObject trailerDataJson = trailers.getJSONObject(i);
            String website = trailerDataJson.getString("site");
            String displayName = "Trailer " + i;


            if (website.equals("YouTube")){
                String url = "https://www.youtube.com/watch?v="+trailerDataJson.getString("key");
                result.add(new MovieTrailersInfo(displayName,url ));
            }
        }

        return result;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    }

    public  static ArrayList<MovieReviewsInfo> GetReviewsFromJsonStrig(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        JSONArray reviews = json.getJSONArray("results");
        ArrayList<MovieReviewsInfo>  result = new ArrayList<MovieReviewsInfo>();

        for (int i = 0; i< reviews.length(); i++){
            JSONObject reviewsDataJson = reviews.getJSONObject(i);
           result.add(new MovieReviewsInfo(reviewsDataJson.getString("author"),reviewsDataJson.getString("content")));
        }

        return result;
    }
}
