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

package com.fjoglar.popularmoviesapp.data.source.local.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fjoglar.popularmoviesapp.data.source.local.provider.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_VIDEO + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ADULT + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now simply drop the table and create a new one. This means if we change the
        // DATABASE_VERSION the table will be dropped.
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
