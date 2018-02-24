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

package com.fjoglar.popularmoviesapp.moviedetail;

import com.fjoglar.popularmoviesapp.base.BasePresenter;
import com.fjoglar.popularmoviesapp.base.BaseView;
import com.fjoglar.popularmoviesapp.data.model.Movie;

import java.util.List;

public interface MovieDetailContract {

    interface View extends BaseView<Presenter> {

        void showMovie(Movie movie);

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends BasePresenter {

        void getMovie();
    }
}