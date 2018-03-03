/*
 * Copyright 2018 Felipe Joglar Santos
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

package com.fjoglar.popularmoviesapp.moviedetail;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fjoglar.popularmoviesapp.R;
import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.source.Repository;
import com.fjoglar.popularmoviesapp.data.source.local.LocalDataSource;
import com.fjoglar.popularmoviesapp.data.source.remote.RemoteDataSource;
import com.fjoglar.popularmoviesapp.movies.MoviesAdapter;
import com.fjoglar.popularmoviesapp.movies.MoviesContract;
import com.fjoglar.popularmoviesapp.movies.MoviesPresenter;
import com.fjoglar.popularmoviesapp.util.schedulers.SchedulerProvider;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View{

    public static final String INTENT_EXTRA_MOVIE = "movie";

    private MovieDetailContract.Presenter mMovieDetailPresenter;

    private ProgressBar mProgressLoading;
    private ImageView mImgCover;
    private ImageView mImgPoster;
    private TextView mTextTitle;
    private TextView mTextReleaseDate;
    private TextView mTextRatingScore;
    private TextView mTextTotalVotes;
    private RatingBar mRatingScore;
    private TextView mTextSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mProgressLoading = findViewById(R.id.progress_loading);
        mImgCover = findViewById(R.id.img_cover);
        mImgPoster = findViewById(R.id.img_poster);
        mTextTitle = findViewById(R.id.text_title);
        mTextReleaseDate = findViewById(R.id.text_release_date);
        mTextRatingScore = findViewById(R.id.text_rating_score);
        mTextTotalVotes = findViewById(R.id.text_total_votes);
        mRatingScore = findViewById(R.id.rating_score);
        mTextSynopsis = findViewById(R.id.text_synopsis);

        mMovieDetailPresenter = new MovieDetailPresenter(this,
                getIntent().getParcelableExtra(INTENT_EXTRA_MOVIE));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMovieDetailPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMovieDetailPresenter.unsubscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(@NonNull MovieDetailContract.Presenter presenter) {
        mMovieDetailPresenter = presenter;
    }

    @Override
    public void showMovie(Movie movie) {
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w1280" +
                        movie.getBackdropPath())
                .into(mImgCover);
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w500" +
                        movie.getPosterPath())
                .into(mImgPoster);
        mTextTitle.setText(movie.getTitle());
        mTextReleaseDate.setText(movie.getReleaseDate());
        mTextRatingScore.setText(String.valueOf(movie.getVoteAverage()));
        mTextTotalVotes.setText(String.valueOf(movie.getVoteCount()));
        mRatingScore.setRating(movie.getVoteAverage() / 2);
        mTextSynopsis.setText(movie.getOverview());
    }

    @Override
    public void showLoading() {
        mProgressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressLoading.setVisibility(View.GONE);
    }
}
