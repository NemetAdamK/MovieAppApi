package com.example.movieapp.Interfaces

import Json4Kotlin_Base_Movies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetTopMovieList {

    @GET("/3/movie/top_rated?api_key=f2caa469e3d1325a79cecec4fbc087b6")

    fun getTopMovieData(): Call<Json4Kotlin_Base_Movies>
}