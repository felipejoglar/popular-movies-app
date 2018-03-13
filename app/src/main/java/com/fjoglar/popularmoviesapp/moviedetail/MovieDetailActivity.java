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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fjoglar.popularmoviesapp.R;
import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.model.Review;
import com.fjoglar.popularmoviesapp.data.model.Video;
import com.fjoglar.popularmoviesapp.data.source.Repository;
import com.fjoglar.popularmoviesapp.data.source.local.LocalDataSource;
import com.fjoglar.popularmoviesapp.data.source.remote.RemoteDataSource;
import com.fjoglar.popularmoviesapp.util.schedulers.SchedulerProvider;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View,
        VideosAdapter.VideoClickListener {

    public static final String INTENT_EXTRA_MOVIE = "movie";

    private MovieDetailContract.Presenter mMovieDetailPresenter;

    private ReviewsAdapter mReviewsAdapter;
    private VideosAdapter mVideosAdapter;

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
    @BindView(R.id.rv_videos)
    RecyclerView mRvVideos;
    @BindView(R.id.text_synopsis)
    TextView mTextSynopsis;
    @BindView(R.id.text_reviews_title)
    TextView mTextReviewsTitle;
    @BindView(R.id.rv_reviews)
    RecyclerView mRvReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);
        setUpReviews();
        setUpVideos();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPresenter();
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
    public void showReviews(List<Review> reviews) {
        if (reviews.isEmpty()) {
            mTextReviewsTitle.setVisibility(View.GONE);
            mRvReviews.setVisibility(View.GONE);
        } else {
            mTextReviewsTitle.setVisibility(View.VISIBLE);
            mRvReviews.setVisibility(View.VISIBLE);
            mReviewsAdapter.setReviewsData(reviews);
        }
    }

    @Override
    public void showVideos(List<Video> videos) {
        if (videos.isEmpty()) {
            mRvVideos.setVisibility(View.GONE);
        } else {
            mRvVideos.setVisibility(View.VISIBLE);
            mVideosAdapter.setVideosData(videos);
        }
    }

    @Override
    public void showLoading() {
        mProgressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressLoading.setVisibility(View.GONE);
    }

    @Override
    public void onVideoClick(Video video) {
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.youtube_url) + video.getKey()));
        Intent chooser = Intent.createChooser(playVideoIntent , "Open With");

        if (playVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    @OnClick(R.id.fab_add_favorite)
    public void addFavorite(View view) {

    }

    private void setUpReviews() {
        mReviewsAdapter = new ReviewsAdapter();

        mRvReviews.setHasFixedSize(true);
        mRvReviews.setLayoutManager(new LinearLayoutManager(this));
        mRvReviews.addItemDecoration(new DividerItemDecoration(mRvReviews.getContext(),
                DividerItemDecoration.VERTICAL));
        mRvReviews.setAdapter(mReviewsAdapter);
    }

    private void setUpVideos() {
        mVideosAdapter = new VideosAdapter(this);

        mRvVideos.setHasFixedSize(true);
        mRvVideos.setLayoutManager(new LinearLayoutManager(this,
                                                           LinearLayoutManager.HORIZONTAL,
                                                           false));
        mRvVideos.setAdapter(mVideosAdapter);
    }

    private void initPresenter() {
        mMovieDetailPresenter = new MovieDetailPresenter(this,
                Repository.getInstance(RemoteDataSource.getInstance(),
                        LocalDataSource.getInstance()),
                SchedulerProvider.getInstance(),
                getIntent().getParcelableExtra(INTENT_EXTRA_MOVIE));
    }
}
