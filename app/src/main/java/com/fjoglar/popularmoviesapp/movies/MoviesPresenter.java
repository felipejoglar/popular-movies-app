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

import android.support.annotation.NonNull;

import com.fjoglar.popularmoviesapp.base.BaseObserver;
import com.fjoglar.popularmoviesapp.data.Constants;
import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.source.DataSource;
import com.fjoglar.popularmoviesapp.data.source.preferences.Preferences;
import com.fjoglar.popularmoviesapp.movies.domain.GetFavoriteMovies;
import com.fjoglar.popularmoviesapp.movies.domain.GetPopularMovies;
import com.fjoglar.popularmoviesapp.movies.domain.GetTopRatedMovies;
import com.fjoglar.popularmoviesapp.util.schedulers.BaseSchedulerProvider;

import java.util.List;

/**
 * {@link MoviesContract.Presenter} that controls communication between views and models of the presentation
 * layer.
 */
public class MoviesPresenter implements MoviesContract.Presenter {

    @NonNull
    private final DataSource mRepository;

    @NonNull
    private final Preferences mPreferences;

    @NonNull
    private final MoviesContract.View mMoviesView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private final GetPopularMovies mGetPopularMovies;
    private final GetTopRatedMovies mGetTopRatedMovies;
    private final GetFavoriteMovies mGetFavoriteMovies;

    public MoviesPresenter(@NonNull DataSource repository,
                           @NonNull Preferences preferences,
                           @NonNull MoviesContract.View moviesView,
                           @NonNull BaseSchedulerProvider schedulerProvider) {
        mRepository = repository;
        mPreferences = preferences;
        mMoviesView = moviesView;
        mSchedulerProvider = schedulerProvider;

        mMoviesView.setPresenter(this);

        mGetPopularMovies = new GetPopularMovies(mRepository,
                mSchedulerProvider.computation(),
                mSchedulerProvider.ui());
        mGetTopRatedMovies = new GetTopRatedMovies(mRepository,
                mSchedulerProvider.computation(),
                mSchedulerProvider.ui());
        mGetFavoriteMovies = new GetFavoriteMovies(mRepository,
                mSchedulerProvider.computation(),
                mSchedulerProvider.ui());
    }

    @Override
    public void subscribe() {

        switch (mPreferences.getCurrentDisplayedMovies()) {
            case Constants.MOVIES_POPULAR:
                getPopularMovies();
                break;
            case Constants.MOVIES_TOP_RATED:
                getTopRatedMovies();
                break;
            case Constants.MOVIES_FAVORITE:
                getFavoriteMovies();
            default:
                break;
        }
    }

    @Override
    public void unsubscribe() {
        mGetPopularMovies.dispose();
        mGetTopRatedMovies.dispose();
        mGetFavoriteMovies.dispose();
    }

    @Override
    public void getPopularMovies() {
        mMoviesView.showLoading();
        mGetPopularMovies.execute(new PopularMoviesListObserver(), null);
    }

    @Override
    public void getTopRatedMovies() {
        mMoviesView.showLoading();
        mGetTopRatedMovies.execute(new TopRatedMoviesListObserver(), null);
    }

    @Override
    public void getFavoriteMovies() {
        mMoviesView.showLoading();
        mGetFavoriteMovies.execute(new FavoriteMoviesListObserver(), null);
    }

    private final class PopularMoviesListObserver extends BaseObserver<List<Movie>> {

        @Override
        public void onNext(List<Movie> movies) {
            mMoviesView.showMovies(movies);
        }

        @Override
        public void onComplete() {
            mMoviesView.hideLoading();
            mPreferences.setCurrentDisplayedMovies(Constants.MOVIES_POPULAR);
        }

        @Override
        public void onError(Throwable e) {

        }
    }

    private final class TopRatedMoviesListObserver extends BaseObserver<List<Movie>> {

        @Override
        public void onNext(List<Movie> movies) {
            mMoviesView.showMovies(movies);
        }

        @Override
        public void onComplete() {
            mMoviesView.hideLoading();
            mPreferences.setCurrentDisplayedMovies(Constants.MOVIES_TOP_RATED);
        }

        @Override
        public void onError(Throwable e) {

        }
    }

    private final class FavoriteMoviesListObserver extends BaseObserver<List<Movie>> {

        @Override
        public void onNext(List<Movie> movies) {
            mMoviesView.showMovies(movies);
        }

        @Override
        public void onComplete() {
            mMoviesView.hideLoading();
            mPreferences.setCurrentDisplayedMovies(Constants.MOVIES_FAVORITE);
        }

        @Override
        public void onError(Throwable e) {

        }
    }
}