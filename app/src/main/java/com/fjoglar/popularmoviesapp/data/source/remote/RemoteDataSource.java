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

package com.fjoglar.popularmoviesapp.data.source.remote;

import android.support.annotation.Nullable;

import com.fjoglar.popularmoviesapp.BuildConfig;
import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.model.MoviesResponse;
import com.fjoglar.popularmoviesapp.data.model.Review;
import com.fjoglar.popularmoviesapp.data.model.ReviewsResponse;
import com.fjoglar.popularmoviesapp.data.model.Video;
import com.fjoglar.popularmoviesapp.data.model.VideosResponse;
import com.fjoglar.popularmoviesapp.data.source.DataSource;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Concrete implementation of a remote data source that adds a latency simulating network.
 */
public class RemoteDataSource implements DataSource {

    private static final String THE_MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/";

    @Nullable
    private static RemoteDataSource INSTANCE = null;
    private TheMovieDbApi mTheMovieDbApi;

    // Prevent direct instantiation.
    private RemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(THE_MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mTheMovieDbApi = retrofit.create(TheMovieDbApi.class);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link RemoteDataSource} instance
     */
    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance()} to create a new instance next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Movie>> getPopularMovies() {
        return mTheMovieDbApi.getPopularMovies(BuildConfig.TMDB_API_KEY)
                .map(MoviesResponse::getMovies);
    }

    @Override
    public Observable<List<Movie>> getTopRatedMovies() {
        return mTheMovieDbApi.getTopRatedMovies(BuildConfig.TMDB_API_KEY)
                .map(MoviesResponse::getMovies);
    }

    @Override
    public Observable<List<Movie>> getFavoriteMovies() {
        // Not used yet
        return null;
    }

    @Override
    public Observable<Movie> getFavoriteMovieById(int movieId) {
        // Not used yet
        return null;
    }

    @Override
    public Observable<List<Review>> getMovieReviews(int movieId) {
        return mTheMovieDbApi.getMovieReviews(movieId, BuildConfig.TMDB_API_KEY)
                .map(ReviewsResponse::getReviews);
    }

    @Override
    public Observable<List<Video>> getMovieVideos(int movieId) {
        return mTheMovieDbApi.getMovieVideos(movieId, BuildConfig.TMDB_API_KEY)
                .map(VideosResponse::getVideos);
    }

    @Override
    public Observable<Boolean> saveMovieAsFavorite(Movie movie) {
        // Not used yet
        return null;
    }

    @Override
    public Observable<Boolean> deleteMovieFromFavorites(int movieId) {
        // Not used yet
        return null;
    }
}