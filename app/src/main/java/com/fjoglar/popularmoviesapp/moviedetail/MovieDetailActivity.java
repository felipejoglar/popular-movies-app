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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fjoglar.popularmoviesapp.R;
import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {

    public static final String INTENT_EXTRA_MOVIE = "movie";

    private MovieDetailContract.Presenter mMovieDetailPresenter;

    @BindView(R.id.progress_loading)
    ProgressBar mProgressLoading;
    @BindView(R.id.img_cover)
    ImageView mImgCover;
    @BindView(R.id.img_poster)
    ImageView mImgPoster;
    @BindView(R.id.text_title)
    TextView mTextTitle;
    @BindView(R.id.text_release_date)
    TextView mTextReleaseDate;
    @BindView(R.id.text_rating_score)
    TextView mTextRatingScore;
    @BindView(R.id.text_total_votes)
    TextView mTextTotalVotes;
    @BindView(R.id.rating_score)
    RatingBar mRatingScore;
    @BindView(R.id.text_synopsis)
    TextView mTextSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

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
