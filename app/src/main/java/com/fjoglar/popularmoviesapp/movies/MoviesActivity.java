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
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fjoglar.popularmoviesapp.R;
import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.source.Repository;
import com.fjoglar.popularmoviesapp.data.source.local.LocalDataSource;
import com.fjoglar.popularmoviesapp.data.source.preferences.AppPreferences;
import com.fjoglar.popularmoviesapp.data.source.remote.RemoteDataSource;
import com.fjoglar.popularmoviesapp.moviedetail.MovieDetailActivity;
import com.fjoglar.popularmoviesapp.util.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View,
        MoviesAdapter.MovieClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String MOVIES_LIST = "movies_list";
    private static final String NAVIGATION = "navigation";

    private MoviesContract.Presenter mMoviesPresenter;
    private MoviesAdapter mMoviesAdapter;
    private List<Movie> mMoviesList;
    private int mNavigation;
    private boolean mForceLoad;

    @BindView(R.id.rv_movies)
    RecyclerView mRvMovies;
    @BindView(R.id.progress_loading)
    ProgressBar mProgressLoading;
    @BindView(R.id.empty_view)
    ConstraintLayout mEmptyView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        ButterKnife.bind(this);

        mForceLoad = true;
        mNavigation = 0;
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mMoviesList = savedInstanceState.getParcelableArrayList(MOVIES_LIST);
            mNavigation = savedInstanceState.getInt(NAVIGATION);
            // If we are in favorites movies we force the load of the new list
            mForceLoad = mNavigation == 2;
        }

        setUpRecyclerView();
        mBottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPresenter();
        if (mForceLoad) {
            mMoviesPresenter.subscribe();
        }
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_LIST, new ArrayList<>(mMoviesAdapter.getList()));
        outState.putInt(NAVIGATION, mNavigation);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                mMoviesPresenter.getPopularMovies();
                break;
            case R.id.top_rated:
                mMoviesPresenter.getTopRatedMovies();
                break;
            case R.id.favorite:
                mMoviesPresenter.getFavoriteMovies();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void setPresenter(@NonNull MoviesContract.Presenter presenter) {
        mMoviesPresenter = presenter;
    }

    @Override
    public void showMovies(List<Movie> movies, int nav) {
        mEmptyView.setVisibility(View.GONE);
        mRvMovies.setVisibility(View.VISIBLE);
        mMoviesAdapter.setMoviesData(movies);
        mBottomNavigation.getMenu().getItem(nav).setChecked(true);
        mNavigation = nav;
    }

    @Override
    public void showEmptyView(int nav) {
        mRvMovies.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mBottomNavigation.getMenu().getItem(nav).setChecked(true);
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
        Intent DetailActivityIntent = new Intent(this, MovieDetailActivity.class);
        DetailActivityIntent.putExtra(MovieDetailActivity.INTENT_EXTRA_MOVIE, movie);
        startActivity(DetailActivityIntent);
    }

    private void setUpRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this,
                this.getResources().getInteger(R.integer.movies_activity_column_number));
        mMoviesAdapter = new MoviesAdapter(this, this);

        mRvMovies.setLayoutManager(layoutManager);
        mRvMovies.setHasFixedSize(true);
        mRvMovies.setAdapter(mMoviesAdapter);
        showMovies(mMoviesList, mNavigation);
    }

    private void initPresenter() {
        mMoviesPresenter = new MoviesPresenter(
                Repository.getInstance(RemoteDataSource.getInstance(),
                        LocalDataSource.getInstance(getApplicationContext())),
                AppPreferences.getInstance(getApplicationContext()),
                this,
                SchedulerProvider.getInstance());
    }
}
