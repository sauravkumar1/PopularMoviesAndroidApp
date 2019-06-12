package com.example.android.popularmovies1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies1.data.FavouriteMovieContract.*;

public class FavouriteMoviesDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "favouriteMoviesCollection.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public FavouriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteMovieEntry.TABLE_NAME + ";");
        // Create a table to hold favourite movies
        final String SQL_CREATE_FAVOURITE_MOVIES_TABLE = "CREATE TABLE " + FavouriteMovieEntry.TABLE_NAME + " (" +
                FavouriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouriteMovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL,"+
                FavouriteMovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                FavouriteMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                FavouriteMovieEntry.COLUMN_MOVIE_RELEASEDATE + " TEXT NOT NULL, " +
                FavouriteMovieEntry.COLUMN_MOVIE_POSTER + " BLOB NOT NULL, " +
                FavouriteMovieEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL, " +
                FavouriteMovieEntry.COLUMN_MOVIE_TRAILERS + " TEXT  , " +
                FavouriteMovieEntry.COLUMN_MOVIE_REVIEWS + " TEXT  " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}