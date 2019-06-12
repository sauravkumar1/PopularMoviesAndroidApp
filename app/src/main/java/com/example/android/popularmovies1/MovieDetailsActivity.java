package com.example.android.popularmovies1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.popularmovies1.data.FavouriteMovieContract;
import com.example.android.popularmovies1.data.FavouriteMoviesDbHelper;
import com.example.android.popularmovies1.utils.BitMapUtil;
import com.example.android.popularmovies1.utils.MovieJsonUtils;
import com.example.android.popularmovies1.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity   implements LoaderManager.LoaderCallbacks {

    private SQLiteDatabase mDb;
    private  MovieInfo movie;
    private static final int LOADERID = 100;

    private boolean isMovieFavourite = false;
    ArrayList<MovieTrailersInfo> mMovieTrailers;
    ArrayList<MovieReviewsInfo> mMovieReviews;

    private MovieTrailersAdapter mAdapter;
    private RecyclerView mRecTrailerView;
    private ImageButton markFavourite;
 private ProgressBar reviewPb;
 private  ProgressBar trailerPb;
    private MovieReviewAdapter mReviewAdapter;
    private RecyclerView mRecReviewsView;
    private  TextView notrailerText;
    private  TextView noreviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_page);

        FavouriteMoviesDbHelper dbHelper = new FavouriteMoviesDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        notrailerText = (TextView) findViewById(R.id.tv_no_trailer);
        noreviewText = (TextView) findViewById(R.id.tv_no_review);
        markFavourite = (ImageButton)findViewById(R.id.ib_mark_favourite);
        reviewPb = (ProgressBar)findViewById(R.id.pb_reviews);
        trailerPb = (ProgressBar)findViewById(R.id.pb_trailer);

        mRecTrailerView = (RecyclerView) findViewById(R.id.rv_movie_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecTrailerView.setLayoutManager(layoutManager);
        mRecTrailerView.setHasFixedSize(true);
        mRecReviewsView = (RecyclerView) findViewById(R.id.rv_movie_reviews);
        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(this);
        mRecReviewsView.setLayoutManager(layoutManagerReviews);
        mRecReviewsView.setHasFixedSize(true);

        SetMovieDetailsPage();


    }

    private  boolean IsMovieFavourite(String id) {
        Cursor cursor = null;
        String sql = "SELECT " + FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " FROM " + FavouriteMovieContract.FavouriteMovieEntry.TABLE_NAME +
                " WHERE " + FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + "=" + id;
        cursor = mDb.rawQuery(sql, null);



        if(cursor.getCount()>0)
        {cursor.close();
            return true;}

        return false;
    }

    private  void SetMovieDetailsPage()
    {
        Intent intentThatStartedThisActivity = getIntent();

        Bundle args = new Bundle();
        if (intentThatStartedThisActivity != null) {
            if(intentThatStartedThisActivity.hasExtra("MOVIE_OBJECT")) {
                movie = new MovieInfo(intentThatStartedThisActivity.getBundleExtra("MOVIE_OBJECT"));

                if (intentThatStartedThisActivity.hasExtra("MOVIE_FAVOURITE")) {
                      markFavourite.setImageDrawable(getResources().getDrawable(R.drawable.favstar));
                      isMovieFavourite = true;
                }
            }


        ImageView moviePoster = (ImageView) findViewById(R.id.iv_poster_detailspage);
        TextView movieTitle = (TextView) findViewById(R.id.tv_title_detailspage);
        TextView movieOverview =  (TextView)findViewById(R.id.tv_overview_detailspage);
        TextView rating = (TextView) findViewById(R.id.tv_rating_detailspage);
        TextView releaseDate =  (TextView)findViewById(R.id.tv_releasedate_detailspage);
            if(movie.moviePoster != null)
            {
                Drawable drawable = new BitmapDrawable(getResources(),  BitMapUtil.getImage(movie.moviePoster));
                moviePoster.setImageDrawable(drawable);
            }
            else {
                Picasso.with(this).load(movie.moviePosterUrl).into(moviePoster);
            }
            movieTitle.setText(movie.title);
            String ratingValue = movie.rating;
            ratingValue = ratingValue + "/10";
            rating.setText(ratingValue);
            movieOverview.setText(movie.overview);


            releaseDate.setText(movie.releaseDate);


                getSupportLoaderManager().restartLoader(LOADERID, args, this);

        }


    }



    public void HandleMarkFavouriteClick(View view) {
        if (IsMovieFavourite(movie.id)) {
             if(  RemvoeMovieFromFavourite(movie.id)) {

                 markFavourite.setImageDrawable(getResources().getDrawable(R.drawable.planestar));
                 Toast.makeText(this, movie.title + " removed from favourite list ", Toast.LENGTH_SHORT).show();
             }
             else
                 Toast.makeText(this,  "Failed to delete " + movie.title , Toast.LENGTH_SHORT).show();

        }
        else
        {
            ImageView moviePosterImageView=   (ImageView)findViewById(R.id.iv_poster_detailspage);
            byte[] moviePoster = BitMapUtil.getBytes(((BitmapDrawable)moviePosterImageView.getDrawable()).getBitmap());

            long id = AddMovieToFavourite(movie,moviePoster);
            if(id > 0) {
                markFavourite.setImageDrawable(getResources().getDrawable(R.drawable.favstar));
                Toast.makeText(this, movie.title + " added to favourite list", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,   "Failed to add " + movie.title , Toast.LENGTH_SHORT).show();

        }

        return;
    }


    private long AddMovieToFavourite(MovieInfo movie, byte[]  posterImage) {

        ContentValues cv = new ContentValues();
        cv.put(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID,movie.id );
        cv.put(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_NAME, movie.title);
        cv.put(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_OVERVIEW, movie.overview);
        cv.put(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_RELEASEDATE, movie.releaseDate);
        cv.put(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_POSTER, posterImage);
        cv.put(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_RATING, movie.rating);
        cv.put(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_TRAILERS, MovieTrailersInfo.arrayToString(movie.trailers));
        cv.put(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_REVIEWS, MovieReviewsInfo.arrayToString(movie.reviews));

        return mDb.insert(FavouriteMovieContract.FavouriteMovieEntry.TABLE_NAME, null, cv);
    }


    private boolean RemvoeMovieFromFavourite(String movieId) {

      return    mDb.delete(FavouriteMovieContract.FavouriteMovieEntry.TABLE_NAME, FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID +
                "=" + movieId,null) >0;
    }

    public Void LoadMovieDataFromApi() {

        String url =  getResources().getString(R.string.movie_detail_url) + movie.id + getResources().getString(R.string.movie_review_endpoint) +
               getResources().getString(R.string.api_key);
        URL reviewUrl = NetworkUtils.buildUrl(url);

        url = getResources().getString(R.string.movie_detail_url) + movie.id + getResources().getString(R.string.movie_trailer_endpoint) +
                getResources().getString(R.string.api_key);
        URL   trailerUrl = NetworkUtils.buildUrl(url);

        try {
            String movieTrailerResponse = NetworkUtils.getResponseFromHttpUrl(trailerUrl);

            mMovieTrailers = MovieJsonUtils.GetTrailersFromJsonStrig(movieTrailerResponse);

            String movieReviewsResponse = NetworkUtils.getResponseFromHttpUrl(reviewUrl);

            mMovieReviews = MovieJsonUtils.GetReviewsFromJsonStrig(movieReviewsResponse);

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private  void LoadFromDb()
    {

          Cursor movieCursor = FetchMovieInfoFromDb();

         ReadFromCursor(movieCursor);


        return ;

    }
    private Cursor FetchMovieInfoFromDb() {

        return mDb.query(
                FavouriteMovieContract.FavouriteMovieEntry.TABLE_NAME,
                new String[]{FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_TRAILERS, FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_REVIEWS},
                FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{movie.id},
                null,
                null,
                FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_RATING
        );
    }

    private  void ReadFromCursor(Cursor cursor)
    {


        if (cursor.moveToFirst()) {

            do {

               String trailersString = cursor.getString(cursor.getColumnIndex(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_TRAILERS));
                ArrayList<MovieTrailersInfo> trailers = MovieTrailersInfo.stringToArray(trailersString);

                String reviewsString = cursor.getString(cursor.getColumnIndex(FavouriteMovieContract.FavouriteMovieEntry.COLUMN_MOVIE_REVIEWS));
                ArrayList<MovieReviewsInfo> reviews = MovieReviewsInfo.stringToArray(reviewsString);
                mMovieTrailers = trailers;
                mMovieReviews = reviews;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ;


    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Void>(this.getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Void loadInBackground() {
                try {
                    if(isMovieFavourite) {
                        LoadFromDb();
                    }
                    else {
                        LoadMovieDataFromApi();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
                return null;
            }
        };

    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        trailerPb.setVisibility(View.GONE);
        reviewPb.setVisibility(View.GONE);
        SetTrailersandReviews();

    }

    private  void SetTrailersandReviews()
    {
        mAdapter = new MovieTrailersAdapter(mMovieTrailers);
        movie.trailers = mMovieTrailers;
        mRecTrailerView.setAdapter(mAdapter);
        ViewCompat.setNestedScrollingEnabled(mRecTrailerView, false);
        if(mMovieTrailers.size()==0)
            notrailerText.setVisibility(View.VISIBLE);
        mReviewAdapter = new MovieReviewAdapter(mMovieReviews);
        movie.reviews = mMovieReviews;
        mRecReviewsView.setAdapter(mReviewAdapter);
        if(mMovieReviews.size()==0)
            noreviewText.setVisibility(View.VISIBLE);
        ViewCompat.setNestedScrollingEnabled(mRecReviewsView, false);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }



}
