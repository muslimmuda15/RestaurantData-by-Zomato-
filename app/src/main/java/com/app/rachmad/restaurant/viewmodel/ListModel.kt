package com.app.rachmad.restaurant.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.app.rachmad.restaurant.`object`.RestaurantSingleData
import com.app.rachmad.restaurant.repository.RestaurantRepository

class ListModel: ViewModel(){
    val restaurantRepository: RestaurantRepository = RestaurantRepository()

    fun restaurant() = restaurantRepository.restaurant()
    fun getMovieList(): List<RestaurantSingleData> = restaurantRepository.restaurantList
    fun getConnectionLive(): LiveData<Int> = restaurantRepository.connectionLive
    fun getError(): String = restaurantRepository.error
}