package com.app.rachmad.restaurant.webservice

import com.app.rachmad.restaurant.`object`.BaseRestaurantData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface AccessSite{
    @Headers("user-key: 4278d9d3231db1310b0688174a36fb51")
    @GET("/api/v2.1/search")
    fun movieSite(
            @Query("collection_id") collectionId: Int,
            @Query("count") count: Int
//    ): Call<String>
    ): Call<BaseRestaurantData>
}