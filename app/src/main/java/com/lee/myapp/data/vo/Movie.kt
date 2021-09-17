package com.lee.myapp.data.vo

// camelCase serializedName 안해도 인식해주나?
data class Movie(

    val id: Int,
    val posterPath: String,
    val releaseDate: String,
    val title: String,


)