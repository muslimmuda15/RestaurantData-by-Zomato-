package com.app.rachmad.restaurant

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.app.rachmad.restaurant.`object`.RestaurantItemData
import com.app.rachmad.restaurant.details.RestaurantDetailsActivity
import com.app.rachmad.retrofit.R

class MainActivity : AppCompatActivity(), ItemFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: RestaurantItemData) {
        val intent = Intent(this, RestaurantDetailsActivity::class.java)
        intent.putExtra("data", item)
        startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = ItemFragment()
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
    }
}
