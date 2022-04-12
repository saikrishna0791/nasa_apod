package com.gs.apod.network

import com.gs.apod.model.ApodItem
import com.gs.apod.room.entity.FavouriteApods
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

//    val BASE_URL : String
//        get() = "https://api.nasa.gov/"

    @GET("planetary/apod?api_key=GUGLnddkq0M31Ed1fLrjYobP1SPgpkNNfhWdh3yU&count=10")
    suspend fun getApodData() : Response<List<FavouriteApods>>

    @GET("planetary/apod?api_key=GUGLnddkq0M31Ed1fLrjYobP1SPgpkNNfhWdh3yU")
    suspend fun getSingleApodData(@Query("date") date : String) : Response<FavouriteApods>
}