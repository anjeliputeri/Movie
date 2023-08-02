package com.example.movie.api

import com.example.movie.response.DetailMovieResponse
import com.example.movie.response.MovieListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int) : Call<MovieListResponse>

    @GET("movie/{movie_id}")
    fun getMoviedetail(@Path("movie_id") id: Int) : Call<DetailMovieResponse>
}