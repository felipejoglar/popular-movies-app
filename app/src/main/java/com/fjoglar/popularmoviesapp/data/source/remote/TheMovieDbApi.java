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

import com.fjoglar.popularmoviesapp.data.model.MoviesResponse;
import com.fjoglar.popularmoviesapp.data.model.ReviewsResponse;
import com.fjoglar.popularmoviesapp.data.model.VideosResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbApi {

    @GET("movie/popular")
    Observable<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Observable<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Observable<ReviewsResponse> getMovieReviews(@Path("movie_id") int movieId,
                                                @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Observable<VideosResponse> getMovieVideos(@Path("movie_id") int movieId,
                                              @Query("api_key") String apiKey);
}
