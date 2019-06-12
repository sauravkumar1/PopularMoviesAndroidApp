package com.example.android.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies1.data.FavouriteMovieContract;
import com.example.android.popularmovies1.data.FavouriteMoviesDbHelper;
import com.example.android.popularmovies1.utils.MovieJsonUtils;
import com.example.android.popularmovies1.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivityFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private ArrayList<MovieInfo> moviesList;
    private SQLiteDatabase mDb;
    ViewGroup _container;
    boolean isFavouriteMoviePage = false;
    ArrayList<String> favouriteMovies;

    GridView gv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        _container= container;
        favouriteMovies = new ArrayList<String>();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gv =  rootView.findViewById(R.id.movies_grid);

        if(!NetworkUtils.isConnectedToInternet(getContext())) {
            String errorMessage = getResources().getString(R.string.no_internet_message);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }

        FavouriteMoviesDbHelper dbHelper = new FavouriteMoviesDbHelper(this.getContext());

        mDb = dbHelper.getWritableDatabase();

            FetchDataBasedOnPopularity();
            return rootView;
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(moviesList==null)
        {
            if(!NetworkUtils.isConnectedToInternet(getContext()))
            {
               String noInternetErrorMessage = getResources().getString(R.string.no_internet_message);
               Toast.makeText(getContext(),noInternetErrorMessage,Toast.LENGTH_LONG).show();
               return  false;
            }
            else
            {
                String noDataErrorMessage = getResources().getString(R.string.error_nodata_message);
                Toast.makeText(getContext(),noDataErrorMessage,Toast.LENGTH_LONG).show();
                return false;
            }
        }
        boolean result =SortData(item);
        return result;
    }

    private  boolean SortData(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.sort_rating:
                FetchDataBasedOnRating();
                return true;
            case R.id.sort_popularity:
                FetchDataBasedOnPopularity();
                return true;
            case R.id.favourite_movies:
               FetchFavoriteMoviesFromDB();
                return true;
            default:
                break;
        }

        return false;
    }
    private void   FetchFavoriteMoviesFromDB()
    {
        new FetchMoviesTask(_container).execute();
        SetGridViewListener();
    }


    private void FetchDataBasedOnRating()
    {
        String  myKey = getResources().getString(R.string.api_key);
        String  movieUrl = getResources().getString(R.string.topratedmovie_url);
        String  posterUrl = getResources().getString(R.string.movie_poster_url);
        String completeUrl= movieUrl.concat(myKey);
        new FetchMoviesTask(_container).execute(completeUrl, posterUrl);
        SetGridViewListener();
        isFavouriteMoviePage =false;

    }

    private void FetchDataBasedOnPopularity()
    {
        String  myKey = getResources().getString(R.string.api_key);
        String  movieUrl = getResources().getString(R.string.polpularmovie_url);
        String  posterUrl = getResources().getString(R.string.movie_poster_url);
        String completeUrl= movieUrl.concat(myKey);
        new FetchMoviesTask(_container).execute(completeUrl, posterUrl);
        SetGridViewListener();
        isFavouriteMoviePage =false;


    }
    private void SetGridViewListener()
    {
            gv.setOnItemClickListener((adapterView, view, i, l) -> {
            MovieInfo movieClicked = movieAdapter.getItem(i);
            movieAdapter.notifyDataSetChanged();
            ExplicitIntent(movieClicked);
        });
    }

    private void ExplicitIntent(MovieInfo movieInfo)
    {

        Context context = getContext();
        Class destinationClass = MovieDetailsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        String details =getResources().getString(R.string.movie_details);
        intentToStartDetailActivity.putExtra("MOVIE_OBJECT", movieInfo.toBundle());
        if(isFavouriteMoviePage || IsMovieFavourite(movieInfo)) {
            intentToStartDetailActivity.putExtra("MOVIE_FAVOURITE", "TRUE");
        }
        startActivity(intentToStartDetailActivity);
    }

    private  boolean IsMovieFavourite(MovieInfo movieInfo) {
        Cursor cursor = null;
        String sql = "SELECT " + FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " FROM " + FavouriteMovieContract.FavouriteMovieEntry.TABLE_NAME +
                " WHERE " + FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + "=" + movieInfo.id;
        cursor = mDb.rawQuery(sql, null);

        if(cursor.getCount()>0)
        {
            cursor.close();
            return true;
        }

        cursor.close();

      return false;
    }


    public void PopulateGridView(ArrayList<MovieInfo> moviesData)
    {
          movieAdapter= new MovieAdapter(this.getContext(),moviesData );
          gv.setAdapter(movieAdapter);

    }

    private   MovieInfo[]  NetworkRequest(String... param)
    {
        if(param == null) return null ;
        String movieUrl = param[0];
        String posterUrl = param[1];

        URL movieURl=  NetworkUtils.buildUrl(movieUrl);


        try {
            String movieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieURl);

            MovieInfo[] simpleJsonMovieInfo  = MovieJsonUtils
                    .getSimpleMovieStringsFromJson(getContext(), movieResponse,posterUrl);



            return simpleJsonMovieInfo;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    private   MovieInfo[]  DbRequest()
    {

       Cursor movieCursor = FetchFavoriteMovies();

        MovieInfo[]  favouiteMovies = ReadFromCursor(movieCursor);


        return favouiteMovies;

    }



private MovieInfo[] ReadFromCursor(Cursor cursor)
{

    MovieInfo[] movieDetailsList =  new MovieInfo[cursor.getCount()];
     int i =0;
    if (cursor.moveToFirst()) {
        //Loop through the table rows
        do {

            String id = cursor.getString(cursor.getColumnIndex(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID));
            String name = cursor.getString(cursor.getColumnIndex(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_NAME));
            byte[] moviePoster = cursor.getBlob(cursor.getColumnIndex(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_POSTER));
            String rating = cursor.getString(cursor.getColumnIndex(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_RATING));
            String releaseDate = cursor.getString(cursor.getColumnIndex(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_RELEASEDATE));
            String overview = cursor.getString(cursor.getColumnIndex(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_OVERVIEW));


            MovieInfo movieDetails = new MovieInfo( id,name,overview,releaseDate,rating,null,moviePoster);

            movieDetailsList[i++]=(movieDetails);
        } while (cursor.moveToNext());
    }

    return movieDetailsList;


}


    private Cursor FetchFavoriteMovies() {


        return mDb.query(
                FavouriteMovieContract.FavouriteMovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_RATING
        );
    }






    public  class FetchMoviesTask extends AsyncTask<String, Void, MovieInfo[]>
    {
        ViewGroup container;
    public  FetchMoviesTask(ViewGroup c)
    {
     container=c;
    }

        @Override
        protected MovieInfo[] doInBackground(String... param) {

            if(param.length==0) {

                return DbRequest();
            }
            else {

              return    NetworkRequest(param);
            }
        }

        @Override
        protected void onPostExecute(MovieInfo[] movieInfo) {
            if(movieInfo!=null)
            {
                super.onPostExecute(movieInfo);
                moviesList= new ArrayList<>(Arrays.asList(movieInfo));
                PopulateGridView(moviesList);


            }

        }

    }



}