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

import android.support.annotation.NonNull;

import com.fjoglar.popularmoviesapp.base.BaseObserver;
import com.fjoglar.popularmoviesapp.data.Constants;
import com.fjoglar.popularmoviesapp.data.model.Movie;
import com.fjoglar.popularmoviesapp.data.model.Review;
import com.fjoglar.popularmoviesapp.data.model.Video;
import com.fjoglar.popularmoviesapp.data.source.DataSource;
import com.fjoglar.popularmoviesapp.moviedetail.domain.DeleteMovieFromFavorites;
import com.fjoglar.popularmoviesapp.moviedetail.domain.GetMovieReviews;
import com.fjoglar.popularmoviesapp.moviedetail.domain.GetMovieVideos;
import com.fjoglar.popularmoviesapp.moviedetail.domain.SaveMovieAsFavorite;
import com.fjoglar.popularmoviesapp.util.schedulers.BaseSchedulerProvider;

import java.util.List;

/**
 * {@link MovieDetailContract.Presenter} that controls communication between views and models of
 * the presentation layer.
 */
public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    @NonNull
    private MovieDetailContract.View mMovieDetailView;

    @NonNull
    private final DataSource mRepository;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    private final Movie mMovie;

    private final GetMovieReviews mGetMovieReviews;
    private final GetMovieVideos mGetMovieVideos;
    private final SaveMovieAsFavorite mSaveMovieAsFavorite;
    private final DeleteMovieFromFavorites mDeleteMovieFromFavorites;

    public MovieDetailPresenter(@NonNull MovieDetailContract.View movieDetailView,
                                @NonNull DataSource repository,
                                @NonNull BaseSchedulerProvider schedulerProvider,
                                @NonNull Movie movie) {
        mMovieDetailView = movieDetailView;
        mRepository = repository;
        mSchedulerProvider = schedulerProvider;
        mMovie = movie;

        mMovieDetailView.setPresenter(this);

        mGetMovieReviews = new GetMovieReviews(mRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mGetMovieVideos = new GetMovieVideos(mRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mSaveMovieAsFavorite = new SaveMovieAsFavorite(mRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());
        mDeleteMovieFromFavorites = new DeleteMovieFromFavorites(mRepository,
                mSchedulerProvider.io(),
                mSchedulerProvider.ui());

    }

    @Override
    public void subscribe() {
        mMovieDetailView.showLoading();
        getMovie();
        getMovieReviews();
        getMovieVideos();
    }

    @Override
    public void unsubscribe() {
        mGetMovieReviews.dispose();
        mGetMovieVideos.dispose();
    }

    @Override
    public void saveOrDeleteMovieAsFavorite() {
        if (mMovie.isFavorite()) {
            deleteMovieFromFavorites();
        } else {
            saveMovieAsFavorite();
        }
    }

    private void getMovie() {
        mMovieDetailView.showMovie(mMovie);
    }

    private void getMovieReviews() {
        mGetMovieReviews.execute(new MovieReviewsObserver(),
                GetMovieReviews.Params.forMovie(mMovie.getId()));
    }

    private void getMovieVideos() {
        mGetMovieVideos.execute(new MovieVideosObserver(),
                GetMovieVideos.Params.forMovie(mMovie.getId()));
    }

    private void saveMovieAsFavorite() {
        mSaveMovieAsFavorite.execute(new SaveFavoriteMovieObserver(),
                SaveMovieAsFavorite.Params.forMovie(mMovie));
    }

    private void deleteMovieFromFavorites() {
        mDeleteMovieFromFavorites.execute(new DeleteFavoriteMovieObserver(),
                DeleteMovieFromFavorites.Params.forMovie(mMovie.getId()));
    }

    private final class MovieReviewsObserver extends BaseObserver<List<Review>> {

        @Override
        public void onNext(List<Review> reviews) {
            mMovieDetailView.showReviews(reviews);
        }

        @Override
        public void onComplete() {
            mMovieDetailView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {

        }
    }

    private final class MovieVideosObserver extends BaseObserver<List<Video>> {

        @Override
        public void onNext(List<Video> videos) {
            mMovieDetailView.showVideos(videos);
        }

        @Override
        public void onComplete() {
            mMovieDetailView.hideLoading();
        }

        @Override
        public void onError(Throwable e) {

        }
    }

    private final class SaveFavoriteMovieObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean isSaved) {
            if (isSaved) {
                mMovieDetailView.updateSavedMovie();
                mMovieDetailView.showMessage(Constants.SAVED_AS_FAVORITE);
            }
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {

        }
    }

    private final class DeleteFavoriteMovieObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean isDeleted) {
            if (isDeleted) {
                mMovieDetailView.updateSavedMovie();
                mMovieDetailView.showMessage(Constants.DELETED_FROM_FAVORITES);
            }
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable e) {

        }
    }
}