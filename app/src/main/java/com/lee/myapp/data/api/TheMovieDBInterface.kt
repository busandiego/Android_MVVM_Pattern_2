package com.lee.myapp.data.api

import com.lee.myapp.data.vo.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBInterface {

// https://developers.themoviedb.org/3
// key: 320e3775e7cc9acdd43aeda6b028a4a3
// https://api.themoviedb.org/3/movie/popular?api_key=320e3775e7cc9acdd43aeda6b028a4a3&language=en-US&page=1
// https://developers.themoviedb.org/3/299534?api_key=320e3775e7cc9acdd43aeda6b028a4a3


    // 인터셉터로 api 키 넣는다
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    // RX JAVA
    // observer data 받음
    // observable data stream

    // Single 이란.


}