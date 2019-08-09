package com.app.rachmad.restaurant.repository

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.app.rachmad.restaurant.`object`.BaseRestaurantData
import com.app.rachmad.restaurant.`object`.RestaurantSingleData
import com.app.rachmad.restaurant.webservice.AccessSite
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor


class RestaurantRepository{
    val client: OkHttpClient

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    var gson = GsonBuilder()
            .setLenient()
            .create()

    val retrofit = Retrofit.Builder()
            .baseUrl("https://developers.zomato.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    var restaurantList: List<RestaurantSingleData> = ArrayList()
    val connectionLive = MutableLiveData<Int>()
    var error = ""

    fun restaurant(){
        val service = retrofit.create(AccessSite::class.java)
        val call = service.movieSite(300641, 30)

        connectionLive.postValue(0)
        Log.d("data", "SERVER LOADING")
//        call.enqueue(object: Callback<String>{
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.d("data", "SERVER Error : " + t.message)
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                Log.d("data", "SERVER SUCCESS : " + response.body())
//            }
//
//        })

//        --------------------------------------------------------------------------------------------------------

        call.enqueue(object: Callback<BaseRestaurantData> {
            override fun onFailure(call: Call<BaseRestaurantData>, t: Throwable) {
                error = t.message?.let { it } ?: run { "Unknow Error" }
                t.printStackTrace()
                connectionLive.postValue(-1)
            }

            override fun onResponse(call: Call<BaseRestaurantData>?, response: Response<BaseRestaurantData>?) {
                response?.let {
                    it.body()?.let {
                        if(response.code() == 200 && response.isSuccessful){
                            Log.d("data", "SERVER SUCCESS")
                            restaurantList = it.restaurants
                            connectionLive.postValue(1)
                        }
                        else{
                            error = "Response unsuccessful for some reason"
                            connectionLive.postValue(-1)
                        }
                    } ?: run {
                        error = "Body is null for some reason"
                        connectionLive.postValue(-1)
                    } ?: run {
                        error = "Response is null for some reason"
                        connectionLive.postValue(-1)
                    }
                }
            }

        })
    }
}

private fun <T> Call<T>.enqueue(callback: Callback<ArrayList<T>>) {

}
