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
import android.util.Log;

import com.fjoglar.popularmoviesapp.base.BaseObserver;
import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.model.MoviesResponse;
import com.fjoglar.popularmoviesapp.data.source.DataSource;
import com.fjoglar.popularmoviesapp.movies.domain.GetMovies;
import com.fjoglar.popularmoviesapp.util.schedulers.BaseSchedulerProvider;

import java.util.List;

public class MoviesPresenter implements MoviesContract.Presenter {

    @NonNull
    private final DataSource mRepository;

    @NonNull
    private final MoviesContract.View mMoviesView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private final GetMovies mGetMovies;

    public MoviesPresenter(@NonNull DataSource repository,
                           @NonNull MoviesContract.View moviesView,
                           @NonNull BaseSchedulerProvider schedulerProvider) {
        mRepository = repository;
        mMoviesView = moviesView;
        mSchedulerProvider = schedulerProvider;

        mMoviesView.setPresenter(this);

        mGetMovies = new GetMovies(mRepository,
                mSchedulerProvider.computation(),
                mSchedulerProvider.ui());
    }

    @Override
    public void subscribe() {
        mMoviesView.showLoading();
        getMovies();
    }

    @Override
    public void unsubscribe() {
        mGetMovies.dispose();
    }

    @Override
    public void getMovies() {
        mGetMovies.execute(new MoviesListObserver());
    }

    private final class MoviesListObserver extends BaseObserver<List<Movie>> {

        @Override
        public void onNext(List<Movie> movies) {
            mMoviesView.showMovies(movies);
        }

        @Override
        public void onComplete() {
            mMoviesView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {

        }
    }
}