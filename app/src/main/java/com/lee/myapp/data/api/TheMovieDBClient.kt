package com.lee.myapp.data.api

import com.google.gson.GsonBuilder
import com.lee.myapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/// 잘못된 주소로 요청했음
/*const val API_KEY = "320e3775e7cc9acdd43aeda6b028a4a3"
const val BASE_URL = "https://developers.themoviedb.org/3/"*/


// 기본URL: https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>
// https://api.themoviedb.org/3/movie/popular?api_key=320e3775e7cc9acdd43aeda6b028a4a3&language=en-US&page=1
// https://api.themoviedb.org/3/movie/299534?api_key=320e3775e7cc9acdd43aeda6b028a4a3
const val API_KEY = "6e63c2317fbe963d76c3bdc2b785f6d1"
const val BASE_URL = "https://api.themoviedb.org/3/"

const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20




const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

object TheMovieDBClient {

    fun getClient(): TheMovieDBInterface {

        val requestInterceptor = Interceptor { chain ->
            // Interceptor take only one argument which is a lambda function so parenthesis can be omitted

            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request =
                chain.request()   // explicitly return a value from with @annotation. lambda always return the value of the last expression implicitly
                    .newBuilder()
                    .url(url)
                    .build()

            return@Interceptor chain.proceed(request)
        }

        var interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addNetworkInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        /*val gson = GsonBuilder().setLenient().create()*/

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDBInterface::class.java)

    }

}