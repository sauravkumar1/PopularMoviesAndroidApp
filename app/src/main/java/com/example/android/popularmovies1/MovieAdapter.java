package com.example.android.popularmovies1;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmovies1.utils.BitMapUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends ArrayAdapter<MovieInfo> {


    public MovieAdapter(@NonNull Context context, @NonNull ArrayList<MovieInfo> moviesInfo) {
        super(context, 0, moviesInfo);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieInfo movieInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movieposter, parent, false);
        }
        ImageView moviePosterView = (ImageView) convertView.findViewById(R.id.movie_poster);
        if(movieInfo.moviePoster != null)
        {

            Drawable drawable = new BitmapDrawable(getContext().getResources(),  BitMapUtil.getImage(movieInfo.moviePoster));
            moviePosterView.setImageDrawable(drawable);
        }
        else
         Picasso.with(getContext()).load(movieInfo.moviePosterUrl).into(moviePosterView);


        return convertView;
    }

}
