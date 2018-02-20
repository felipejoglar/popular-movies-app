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

import android.support.annotation.Nullable;

import com.fjoglar.popularmoviesapp.data.source.DataSource;

import io.reactivex.Observable;

/**
 * Concrete implementation of a local data source.
 */
public class LocalDataSource implements DataSource {

    @Nullable
    private static LocalDataSource INSTANCE = null;

    // Prevent direct instantiation.
    private LocalDataSource() {

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @return the {@link LocalDataSource} instance
     */
    public static LocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource();
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
    public Observable<String[]> getMovies() {

        final String POSTER_URL = "https://image.tmdb.org/t/p/w500/q0R4crx2SehcEEQEkYObktdeFy.jpg";
        String[] fakeMovies = new String[10];

        for (int i = 0; i < 10; i++) {
            fakeMovies[i] = POSTER_URL;
        }

        return Observable.just(fakeMovies);
    }
}