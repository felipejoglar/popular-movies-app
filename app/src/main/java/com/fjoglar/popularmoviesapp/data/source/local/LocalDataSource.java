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

package com.fjoglar.popularmoviesapp.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.model.Review;
import com.fjoglar.popularmoviesapp.data.model.Video;
import com.fjoglar.popularmoviesapp.data.source.DataSource;
import com.fjoglar.popularmoviesapp.data.source.local.provider.MovieContract.MovieEntry;
import com.fjoglar.popularmoviesapp.util.RepositoryUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * Concrete implementation of a local data source.
 */
public class LocalDataSource implements DataSource {

    @Nullable
    private static LocalDataSource INSTANCE = null;

    private Context mContext;

    // Prevent direct instantiation.
    private LocalDataSource(Context context) {
        mContext = context;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link LocalDataSource} instance
     */
    public static LocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(Context)} to create a new instance next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Movie>> getPopularMovies() {
        // Not used yet
        return null;
    }

    @Override
    public Observable<List<Movie>> getTopRatedMovies() {
        // Not used yet
        return null;
    }

    @Override
    public Observable<List<Movie>> getFavoriteMovies() {
        return Observable.fromCallable(() -> {
            Cursor result = mContext.getContentResolver().query(
                    MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            return RepositoryUtils.toMoviesList(result);
        });
    }

    @Override
    public Observable<Movie> getFavoriteMovieById(int movieId) {
        return Observable.fromCallable(() -> {
            Cursor result = mContext.getContentResolver().query(
                    MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(movieId)).build(),
                    null,
                    null,
                    null,
                    null);
            return RepositoryUtils.toMovie(result);
        });
    }

    @Override
    public Observable<List<Review>> getMovieReviews(int movieId) {
        // Not used yet
        return null;
    }

    @Override
    public Observable<List<Video>> getMovieVideos(int movieId) {
        // Not used yet
        return null;
    }

    @Override
    public Observable<Boolean> saveMovieAsFavorite(Movie movie) {
        return Observable.fromCallable(() -> {
            ContentValues movieContentValues = new ContentValues();

            /*
             * Sets the values of each column and inserts the movie.
             */
            movieContentValues.put(MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
            movieContentValues.put(MovieEntry.COLUMN_ID, movie.getId());
            movieContentValues.put(MovieEntry.COLUMN_VIDEO, movie.hasVideo());
            movieContentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            movieContentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
            movieContentValues.put(MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
            movieContentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            movieContentValues.put(MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
            movieContentValues.put(MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
            movieContentValues.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
            movieContentValues.put(MovieEntry.COLUMN_ADULT, movie.isAdult());
            movieContentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            movieContentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

            Uri resultUri = mContext.getContentResolver().insert(
                    MovieEntry.CONTENT_URI,
                    movieContentValues);

            return !Uri.EMPTY.equals(resultUri);
        });
    }

    @Override
    public Observable<Boolean> deleteMovieFromFavorites(int movieId) {
        return Observable.fromCallable(() -> {
            int rowsDeleted;

            rowsDeleted = mContext.getContentResolver().delete(
                    MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(movieId)).build(),
                    null,
                    null);
            return rowsDeleted != 0;
        });
    }
}
