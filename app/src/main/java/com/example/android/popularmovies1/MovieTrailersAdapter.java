/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies1;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;


class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailerViewHolder> {

    private static final String TAG = MovieTrailersAdapter.class.getSimpleName();



    private  ArrayList<MovieTrailersInfo>  movieTrailersInfos;




    public MovieTrailersAdapter(ArrayList<MovieTrailersInfo> trailersInfos) {
        movieTrailersInfos = trailersInfos;

    }


    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_trailers;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieTrailerViewHolder viewHolder = new MovieTrailerViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return movieTrailersInfos.size();
    }


    class MovieTrailerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView trailerDisplayName;




        public MovieTrailerViewHolder(View itemView) {
            super(itemView);

            trailerDisplayName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            int index = listIndex+1;
            String displayName = "Trailer " + index;
            trailerDisplayName.setText(displayName);
        }


           @Override
            public void onClick(View v) {
            Log.d("some error", "error occured");
            int clickedPosition = getAdapterPosition();


               Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieTrailersInfos.get(clickedPosition).Trailer_Url));

               try {
                   v.getContext().startActivity(appIntent);


               } catch (ActivityNotFoundException ex) {

               }


        }
    }
}
