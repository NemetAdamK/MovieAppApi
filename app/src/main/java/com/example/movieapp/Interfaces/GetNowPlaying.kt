package com.example.movieapp.Interfaces

import Json4Kotlin_Base_Movies
import retrofit2.Call
import retrofit2.http.GET

interface GetNowPlaying {

    @GET("/3/movie/now_playing?api_key=f2caa469e3d1325a79cecec4fbc087b6")

    fun getTopMovieData(): Call<Json4Kotlin_Base_Movies>
}