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

package com.fjoglar.popularmoviesapp.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.model.Review;
import com.fjoglar.popularmoviesapp.data.model.Video;

import java.util.List;

import io.reactivex.Observable;

public class Repository implements DataSource {

    @Nullable
    private static volatile Repository INSTANCE = null;

    @NonNull
    private final DataSource mRemoteDataSource;

    @NonNull
    private final DataSource mLocalDataSource;

    // Prevent direct instantiation.
    private Repository(@NonNull DataSource remoteDataSource,
                       @NonNull DataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param remoteDataSource the backend data source
     * @param localDataSource  the device storage data source
     * @return the {@link Repository} instance
     */
    public static Repository getInstance(@NonNull DataSource remoteDataSource,
                                         @NonNull DataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new Repository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(DataSource, DataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Movie>> getPopularMovies() {
        if (false) {
            // Not used yet
            return mLocalDataSource.getPopularMovies();
        } else {
            return mRemoteDataSource.getPopularMovies();
        }
    }

    @Override
    public Observable<List<Movie>> getTopRatedMovies() {
        if (false) {
            // Not used yet
            return mLocalDataSource.getTopRatedMovies();
        } else {
            return mRemoteDataSource.getTopRatedMovies();
        }
    }

    @Override
    public Observable<List<Movie>> getFavoriteMovies() {
        return mLocalDataSource.getFavoriteMovies();
    }

    @Override
    public Observable<Movie> getFavoriteMovieById(int movieId) {
        return mLocalDataSource.getFavoriteMovieById(movieId);
    }

    @Override
    public Observable<List<Review>> getMovieReviews(int movieId) {
        if (false) {
            // Not used yet
            return mLocalDataSource.getMovieReviews(movieId);
        } else {
            return mRemoteDataSource.getMovieReviews(movieId);
        }
    }

    @Override
    public Observable<List<Video>> getMovieVideos(int movieId) {
        if (false) {
            // Not used yet
            return mLocalDataSource.getMovieVideos(movieId);
        } else {
            return mRemoteDataSource.getMovieVideos(movieId);
        }
    }

    @Override
    public Observable<Boolean> saveMovieAsFavorite(Movie movie) {
        return mLocalDataSource.saveMovieAsFavorite(movie);
    }

    @Override
    public Observable<Boolean> deleteMovieFromFavorites(int movieId) {
        return mLocalDataSource.deleteMovieFromFavorites(movieId);
    }
}