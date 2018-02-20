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

package com.fjoglar.popularmoviesapp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.fjoglar.popularmoviesapp.R;
import com.fjoglar.popularmoviesapp.data.source.Repository;
import com.fjoglar.popularmoviesapp.data.source.local.LocalDataSource;
import com.fjoglar.popularmoviesapp.data.source.remote.RemoteDataSource;
import com.fjoglar.popularmoviesapp.util.schedulers.SchedulerProvider;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private static final int COLUMN_NUMBER = 2;

    private MainContract.Presenter mMainPresenter;
    private MoviesListAdapter mMoviesListAdapter;

    private RecyclerView mRvMoviesList;
    private ProgressBar mProgressLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRvMoviesList = findViewById(R.id.rv_movieslist);
        mProgressLoading = findViewById(R.id.progress_loading);

        mMainPresenter = new MainPresenter(
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
        mMainPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainPresenter.unsubscribe();
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
    public void setPresenter(@NonNull MainContract.Presenter presenter) {
        mMainPresenter = presenter;
    }

    @Override
    public void showMoviesList(String[] moviesList) {
        mMoviesListAdapter.setWMoviesData(moviesList);
    }

    @Override
    public void showLoading() {
        mProgressLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressLoading.setVisibility(View.GONE);
    }

    private void setUpRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMN_NUMBER);
        mMoviesListAdapter = new MoviesListAdapter(this);

        mRvMoviesList.setLayoutManager(layoutManager);
        mRvMoviesList.setHasFixedSize(true);
        mRvMoviesList.setAdapter(mMoviesListAdapter);
    }
}
