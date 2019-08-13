package com.app.rachmad.restaurant.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.app.rachmad.restaurant.GlideApp
import com.app.rachmad.restaurant.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        profile_toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setSupportActionBar(profile_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "About"
        supportActionBar!!.elevation = 0F

        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        getSupportActionBar()?.title = Html.fromHtml("<font color='#ffffff'>About</font>")

        title_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

//        GlideApp.with(image_profile)
//                .load("https://www.dicoding.com/images/small/avatar/20190812171621abb4c8bdd106c968ebcbf8b0c87e8e4e.jpg")
//                .centerCrop()
//                .thumbnail(GlideApp.with(image_profile).load(R.drawable.loading))
//                .error(R.drawable.ic_error_outline_black_24dp)
//                .into(image_profile)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
