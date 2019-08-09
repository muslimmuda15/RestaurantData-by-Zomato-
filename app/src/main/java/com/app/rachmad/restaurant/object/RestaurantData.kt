package com.app.rachmad.restaurant.`object`

import java.io.Serializable

data class BaseRestaurantData(
        val results_found: Int,
        val results_start: Int,
        val results_shown: Int,
        val restaurants: ArrayList<RestaurantSingleData>
)

data class RestaurantSingleData(
        val restaurant: RestaurantItemData
)

data class RestaurantItemData (
        val id: String?,
        val name: String?,
        val url: String?,
        val location: LocationRestaurantData?,
        val timings: String?,
        val average_cost_for_two: String?,
        val price_range: String?,
        val currency: String?,
        val highlights: ArrayList<String>?,
        val thumb: String?,
        val featured_image: String?,
        val photos_url: String?,
        val menu_url: String?,
        val events_url: String?,
        val user_rating: UserRatingData?,
        val has_online_delivery: String?,
        val is_delivering_now: String?,
        val has_table_booking: String?,
        val book_url: String?,
        val deeplink: String?,
        val cuisines: String?,
        val all_reviews_count: String?,
        val photo_count: String?,
        val phone_numbers: String?,
        val photos: ArrayList<Photo>?,
        val all_reviews: SingleReview?
): Serializable

data class SingleReview(
        val reviews: ArrayList<AllReviewRestaurantData>
): Serializable

data class AllReviewRestaurantData(
        val rating: String?,
        val review_text: String?,
        val id: String?,
        val rating_color: String?,
        val review_time_friendly: String?,
        val rating_text: String?,
        val timestamp: String?,
        val likes: String?,
        val user: UserPhotoRestaurantData?,
        val comments_count: String?
): Serializable

data class Photo (
        val photo: PhotoRestaurantData?
): Serializable

data class PhotoRestaurantData(
        val id: String?,
        val url: String?,
        val thumb_url: String?,
        val user: UserPhotoRestaurantData?,
        val res_id: String?,
        val caption: String?,
        val timestamp: String?,
        val friendly_time: String?,
        val width: String?,
        val height: String?,
        val comments_count: String?,
        val likes_count: String?
): Serializable

data class UserPhotoRestaurantData(
        val name: String?,
        val zomato: String?,
        val zomato_handle: String?,
        val foodie_level: String?,
        val foodie_level_num: String?,
        val foodie_color: String?,
        val profile_url: String?,
        val profile_deeplink: String?,
        val profile_image: String?
): Serializable

data class UserRatingData(
        val aggregate_rating: String?,
        val rating_text: String?,
        val rating_color: String?,
        val votes: String?
): Serializable

data class LocationRestaurantData(
        val address: String?,
        val locality: String?,
        val city: String?,
        val latitude: String?,
        val longitude: String?,
        val zipcode: String?,
        val country_id: String?
): Serializable