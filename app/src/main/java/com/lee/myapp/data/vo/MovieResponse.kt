package com.lee.myapp.data.vo

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int,
    @SerializedName("results")
    val movieList: List<MovieResult>,
    val totalPages: Int,
    val totalResults: Int
)