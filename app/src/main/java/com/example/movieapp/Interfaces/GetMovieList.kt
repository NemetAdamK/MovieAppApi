package com.example.movieapp.Interfaces


import Json4Kotlin_Base_Movies
import com.example.movieapp.API.TrailerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GetMovieList {

    @GET("search/movie?api_key=f2caa469e3d1325a79cecec4fbc087b6")

    fun getAllData(@Query("query") user: String): Call<Json4Kotlin_Base_Movies>

    @GET("movie/{movie_id}/videos?api_key=f2caa469e3d1325a79cecec4fbc087b6")
    fun getTrailer(@Path("movie_id") id: String): Call<TrailerResponse>


}