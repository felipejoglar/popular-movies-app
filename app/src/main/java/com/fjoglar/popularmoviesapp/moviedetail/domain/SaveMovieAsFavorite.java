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

package com.fjoglar.popularmoviesapp.moviedetail.domain;

import com.fjoglar.popularmoviesapp.UseCase;
import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.source.DataSource;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * saving a {@link Movie} as favorite.
 */
public class SaveMovieAsFavorite extends UseCase<Boolean, SaveMovieAsFavorite.Params> {

    private final DataSource mRepository;

    public SaveMovieAsFavorite(DataSource repository,
                               Scheduler threadExecutor,
                               Scheduler postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    public Observable<Boolean> buildUseCaseObservable(Params params) {
        return mRepository.saveMovieAsFavorite(params.movie);
    }

    public static final class Params {

        private final Movie movie;

        private Params(Movie movie) {
            this.movie = movie;
        }

        public static Params forMovie(Movie movie) {
            return new Params(movie);
        }
    }
}