package com.lee.myapp.data.vo

import com.google.gson.annotations.SerializedName

// camelCase serializedName 안해도 인식해주나?
data class Movie(

    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,


)