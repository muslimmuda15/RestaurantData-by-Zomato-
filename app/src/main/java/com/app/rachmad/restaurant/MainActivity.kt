package com.app.rachmad.restaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.app.rachmad.restaurant.`object`.RestaurantItemData
import com.app.rachmad.restaurant.details.RestaurantDetailsActivity
import com.app.rachmad.restaurant.profile.AboutActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ItemFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: RestaurantItemData) {
        val intent = Intent(this, RestaurantDetailsActivity::class.java)
        intent.putExtra("data", item)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        main_toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.title = Html.fromHtml("<font color='#ffffff'>${getString(R.string.app_name)}</font>")

        val fragment = ItemFragment()
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.person -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
