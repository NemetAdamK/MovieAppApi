package com.example.movieapp.API

import com.google.gson.annotations.SerializedName

data class TrailerResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<TrailerResults>
)
