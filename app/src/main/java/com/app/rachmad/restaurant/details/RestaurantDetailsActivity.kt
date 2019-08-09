package com.app.rachmad.restaurant.details

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import com.app.rachmad.restaurant.`object`.RestaurantItemData
import com.app.rachmad.retrofit.R
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import kotlinx.android.synthetic.main.custom_layout_phone.view.*

class RestaurantDetailsActivity : AppCompatActivity(), BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    override fun onSliderClick(slider: BaseSliderView?) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onStop() {
        slider.stopAutoCycle()
        super.onStop()
    }

    var restaurantItemData: RestaurantItemData? = null

    private fun initialize(){
        restaurantItemData = intent.getSerializableExtra("data") as RestaurantItemData
        Log.d("data", restaurantItemData.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle(restaurantItemData?.name)


        initialize()

        restaurantItemData?.let {
            val photo = it.photos
            photo?.forEach {
                val textSliderView = TextSliderView(this)
                textSliderView.image(it.photo?.url)
                        .description("by : " + it.photo?.user?.name)
                        .setScaleType(BaseSliderView.ScaleType.CenterInside)
                        .setOnSliderClickListener(this)

                slider.addSlider(textSliderView)
            }
            slider.setPresetTransformer(SliderLayout.Transformer.Accordion)
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            slider.setCustomAnimation(DescriptionAnimation())
            slider.setDuration(3000)
            slider.addOnPageChangeListener(this)

            setContent(it)
        }
    }

    private fun setContent(item: RestaurantItemData){
        name.text = item.name
        item.location?.let {
            if(it.address.isNullOrBlank()){
                address_layout.visibility = ViewGroup.GONE
            }else {
                address_layout.visibility = ViewGroup.VISIBLE
                address.text = it.address
            }
        } ?: run {
            address_layout.visibility = ViewGroup.GONE
        }

        item.phone_numbers?.let {
            val phoneArray: List<String> = it.split(Regex(",\\s*"))
            if(phoneArray.size > 0) {
                phone_layout.visibility = ViewGroup.VISIBLE
                phoneArray.forEach {
                    it?.let { number ->
                        if (!Regex("\\w").matches(number)) {
                            val view = layoutInflater.inflate(R.layout.custom_layout_phone, null)
                            view.phone_number.text = number

                            view.phone_call.setOnClickListener {
                                val callIntent = Intent(Intent.ACTION_DIAL)
                                callIntent.data = Uri.parse("tel:$number")
                                startActivity(callIntent)
                            }
                            phone_list.addView(view)
                        }
                    }
                }
            }
            else{
                phone_layout.visibility = ViewGroup.GONE
            }
        } ?: run {
            phone_layout.visibility = ViewGroup.GONE
        }

        item.timings?.let {
            if(it.isNullOrBlank()){
                time_layout.visibility = ViewGroup.GONE
            }
            else{
                time_layout.visibility = ViewGroup.VISIBLE
                time.text = it
            }
        } ?: run {
            time_layout.visibility = ViewGroup.GONE
        }

        item.url?.let {
            if(it.isNullOrBlank()){
                url_layout.visibility = ViewGroup.GONE
            }
            else{
                url_layout.visibility = ViewGroup.VISIBLE
                url.text = Html.fromHtml("<a href=\""+it+"\">Website</a>")
                url.isClickable = true
                url.movementMethod = LinkMovementMethod.getInstance()

            }
        } ?: run {
            url_layout.visibility = ViewGroup.GONE
        }

        item.book_url?.let {
            if(it.isNullOrBlank()){
                book_layout.visibility = ViewGroup.GONE
            }
            else{
                book_layout.visibility = ViewGroup.VISIBLE
                book_url.text = Html.fromHtml("<a href=\""+it+"\">Book Now</a>")
                book_url.isClickable = true
                book_url.movementMethod = LinkMovementMethod.getInstance()

            }
        } ?: run {
            book_layout.visibility = ViewGroup.GONE
        }

        item.events_url?.let {
            if(it.isNullOrBlank()){
                event_layout.visibility = ViewGroup.GONE
            }
            else{
                event_layout.visibility = ViewGroup.VISIBLE
                event_url.text = Html.fromHtml("<a href=\""+it+"\">See Event</a>")
                event_url.isClickable = true
                event_url.movementMethod = LinkMovementMethod.getInstance()

            }
        } ?: run {
            event_layout.visibility = ViewGroup.GONE
        }

        item.highlights?.let {
            if(it.size > 0){
                facility_layout_layout.visibility = ViewGroup.VISIBLE
                it.forEach {
                    val chip = Chip(facility_collection.context)
                    chip.text = it
                    chip.textSize = resources.getDimension(R.dimen.normal_text)

                    facility_collection.addView(chip)
                }
            }
            else{
                facility_layout_layout.visibility = ViewGroup.GONE
            }
        } ?: run {
            facility_layout_layout.visibility = ViewGroup.GONE
        }

        item.user_rating?.let {
            it.aggregate_rating?.let {
                aggregate_rating.text = it
                rating_star.rating = it.toFloat()
            } ?: run {
                aggregate_rating.text = "0"
            }
            it.rating_text?.let {
                rating_description.text = it
            } ?: run {
                rating_description.text = ""
            }
            it.votes?.let {
                review_count.text = it + " reviews"
            } ?: run {
                review_count.text = "0 reviews"
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
