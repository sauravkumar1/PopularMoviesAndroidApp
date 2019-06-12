package com.example.android.popularmovies1;

import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;

public class MovieInfo  implements Parcelable {
    public static String ID = "id";
    public  static String  TITLE = "title";
    public static String OVERVIEW = "overview";
    public static String RELEASE_DATE ="releasedate";
    public static String RATING= "rating";
    public static String POSTERURL= "posterurl";
    public static String POSTERIMAGE= "posteimage";




    public String id;
    public String  title;
    public String overview;
    public String releaseDate;
    public String rating;
    public String moviePosterUrl;
    public byte[] moviePoster;
    public ArrayList<MovieTrailersInfo> trailers;
    public ArrayList<MovieReviewsInfo> reviews;

    public MovieInfo(String ID, String Title, String Overview, String Releasedate, String Rating, String MoviePosterUrl, byte[] MoviePoster) {
        trailers = new ArrayList<>();
        reviews = new ArrayList<>();
        id = ID;
        title =Title;
        overview = Overview;
        releaseDate = Releasedate;
        rating = Rating;
        moviePosterUrl = MoviePosterUrl;
        moviePoster = MoviePoster;

    }

    protected MovieInfo(Parcel in) {
        id = in.readString();
        title = in.readString();
        overview = in.readString();

        releaseDate = in.readString();
        rating = in.readString();

        moviePosterUrl = in.readString();
        moviePoster = in.createByteArray();
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(rating);
        parcel.writeString(moviePosterUrl);
        parcel.writeByteArray(moviePoster);

    }

    MovieInfo(Bundle bundle)
    {
        this(bundle.getString(ID),
                bundle.getString(TITLE),
                bundle.getString(OVERVIEW),

                bundle.getString(RELEASE_DATE),
                bundle.getString(RATING),
                bundle.getString(POSTERURL),
                bundle.getByteArray(POSTERIMAGE)
                );

    }

    Bundle toBundle()
    {
        Bundle bundle = new Bundle();
        bundle.putString(ID, id);
        bundle.putString(TITLE, title);
        bundle.putString(OVERVIEW, overview);
        bundle.putString(RELEASE_DATE, releaseDate);
        bundle.putString(RATING, rating);
        bundle.putString(POSTERURL, moviePosterUrl);
        bundle.putByteArray(POSTERIMAGE,moviePoster);
        return bundle;
    }
}
