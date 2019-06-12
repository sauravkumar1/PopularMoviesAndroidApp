package com.example.android.popularmovies1.data;

import android.provider.BaseColumns;

import java.sql.Blob;

public class FavouriteMovieContract {

    public static final class FavouriteMovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "favouriteMovies";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_MOVIE_NAME = "movieName";
        public static final String COLUMN_MOVIE_OVERVIEW = "movieOverview";
        public static final String COLUMN_MOVIE_RELEASEDATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        public static final String COLUMN_MOVIE_RATING = "movieRating";
        public static final String COLUMN_MOVIE_TRAILERS = "movieTrailers";
        public static final String COLUMN_MOVIE_REVIEWS = "movieReviews";

    }
}
