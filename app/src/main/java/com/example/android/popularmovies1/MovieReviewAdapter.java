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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder> {



    private  ArrayList<MovieReviewsInfo>  movieReviewsInfos;




    public MovieReviewAdapter(ArrayList<MovieReviewsInfo> reviewsInfos) {
        movieReviewsInfos = reviewsInfos;

    }


    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_reviews;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieReviewViewHolder viewHolder = new MovieReviewViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {

        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return movieReviewsInfos.size();
    }


    class MovieReviewViewHolder extends RecyclerView.ViewHolder
        {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView author;
        // Will display which ViewHolder is displaying this data
        TextView reviewComments;


        public MovieReviewViewHolder(View itemView) {
            super(itemView);

            author = (TextView) itemView.findViewById(R.id.tv_autor);
            reviewComments = (TextView) itemView.findViewById(R.id.tv_autor_review);

        }

        void bind(int listIndex) {
            author.setText(movieReviewsInfos.get(listIndex).AuthorName);
            reviewComments.setText(movieReviewsInfos.get(listIndex).ReviewDetail);
        }


    }
}
