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

package com.fjoglar.popularmoviesapp.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fjoglar.popularmoviesapp.R;
import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.source.Repository;
import com.fjoglar.popularmoviesapp.data.source.local.LocalDataSource;
import com.fjoglar.popularmoviesapp.data.source.remote.RemoteDataSource;
import com.fjoglar.popularmoviesapp.moviedetail.MovieDetailActivity;
import com.fjoglar.popularmoviesapp.util.schedulers.SchedulerProvider;

import java.util.List;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View,
        MoviesAdapter.MovieClickListener {

    private static final int COLUMN_NUMBER = 2;

    private Toast mToast;

    private MoviesContract.Presenter mMoviesPresenter;
    private MoviesAdapter mMoviesAdapter;

    private RecyclerView mRvMovies;
    private ProgressBar mProgressLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRvMovies = findViewById(R.id.rv_movies);
        mProgressLoading = findViewById(R.id.progress_loading);

        mMoviesPresenter = new MoviesPresenter(
                Repository.getInstance(RemoteDataSource.getInstance(),
                        LocalDataSource.getInstance()),
                this,
                SchedulerProvider.getInstance());

        setUpRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMoviesPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMoviesPresenter.unsubscribe();
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
    public void setPresenter(@NonNull MoviesContract.Presenter presenter) {
        mMoviesPresenter = presenter;
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mMoviesAdapter.setMoviesData(movies);
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
    public void onMovieClick(Movie movie) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this,
                movie.getTitle(),
                Toast.LENGTH_LONG);
        mToast.show();

        Intent DetailActivityIntent = new Intent(this, MovieDetailActivity.class);
        DetailActivityIntent.putExtra(MovieDetailActivity.INTENT_EXTRA_MOVIE, movie);
        startActivity(DetailActivityIntent);
    }

    private void setUpRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMN_NUMBER);
        mMoviesAdapter = new MoviesAdapter(this, this);

        mRvMovies.setLayoutManager(layoutManager);
        mRvMovies.setHasFixedSize(true);
        mRvMovies.setAdapter(mMoviesAdapter);
    }
}
