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

package com.fjoglar.popularmoviesapp.data.source.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fjoglar.popularmoviesapp.data.Constants;

public class AppPreferences implements Preferences {

    private static final String PREF_KEY_CURRENT_DISPLAYED_MOVIES = "current_displayed_movies";

    private static volatile AppPreferences INSTANCE;
    private final SharedPreferences mPrefs;

    private AppPreferences(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static AppPreferences getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppPreferences(context);
        }
        return INSTANCE;
    }

    @Override
    public String getCurrentDisplayedMovies() {
        return mPrefs.getString(PREF_KEY_CURRENT_DISPLAYED_MOVIES, Constants.MOVIES_POPULAR);
    }

    @Override
    public void setCurrentDisplayedMovies(String currentDisplayedMovies) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_DISPLAYED_MOVIES, currentDisplayedMovies).apply();
    }
}
