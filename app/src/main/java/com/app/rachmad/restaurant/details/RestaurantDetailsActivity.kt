package com.app.rachmad.restaurant.details

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import com.app.rachmad.restaurant.GlideApp
import com.app.rachmad.restaurant.`object`.RestaurantItemData
import com.app.rachmad.retrofit.R
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import kotlinx.android.synthetic.main.activity_restaurant_details.view.*
import kotlinx.android.synthetic.main.custom_layout_phone.view.*
import kotlinx.android.synthetic.main.custom_layout_review.view.*
import kotlinx.android.synthetic.main.custom_layout_review.view.rating_description
import kotlinx.android.synthetic.main.custom_layout_review.view.time

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

        initialize()

        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        getSupportActionBar()?.setDisplayShowTitleEnabled(true)
//        restaurant_name.text = restaurantItemData?.name

        getSupportActionBar()?.title = Html.fromHtml("<font color='#ffffff'>${restaurantItemData?.name}</font>")

        Log.d("data", "RESTAURANT NAME = " + restaurantItemData?.name)

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

            toolbar.background.alpha = 0

            var imageHeight = 0
            slider.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    slider.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    imageHeight = slider.measuredHeight
                }
            })

            scroll.viewTreeObserver.addOnScrollChangedListener {
                Log.d("data", "SCROLL Y : " + scroll.scrollY)
                if(scroll.scrollY == 0){
                    toolbar.background.alpha = 0
                    Log.d("scroll", "transparent : " + 0)
                }
                else if(scroll.scrollY > 0){
                    val transparent = ((scroll.scrollY.toFloat() / imageHeight.toFloat()) * 255.toFloat()).toInt()
                    Log.d("scroll", "(${scroll.scrollY.toFloat()} / ${imageHeight.toFloat()}) * ${255.toFloat()}")
                    Log.d("scroll", "transparent : " + transparent)
                    if (transparent <= 255) {
                        toolbar.background.alpha = transparent
                    }
                }
            }
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

        item.all_reviews?.reviews?.forEach { review ->
            val view = layoutInflater.inflate(R.layout.custom_layout_review, null)

            review.review.user?.let {
                GlideApp.with(view.user_photo)
                    .load(it.profile_image)
                    .centerCrop()
                    .thumbnail(GlideApp.with(view.user_photo).load(R.drawable.loading))
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(view.user_photo)

                view.user_name.text = it.name
                view.star.rating = review.review.rating?.toFloat()?.let { it } ?: run { 0F }
                view.time.text = review.review.review_time_friendly
                view.rating_description.text = review.review.review_text

                view.rating_description.setOnClickListener {
                    if(view.rating_description.ellipsize == TextUtils.TruncateAt.END) {
                        view.rating_description.maxLines = Integer.MAX_VALUE
                        view.rating_description.ellipsize = null
                    }
                    else{
                        view.rating_description.maxLines = 3
                        view.rating_description.ellipsize = TextUtils.TruncateAt.END
                    }
                }

                reviews_collection.addView(view)
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
