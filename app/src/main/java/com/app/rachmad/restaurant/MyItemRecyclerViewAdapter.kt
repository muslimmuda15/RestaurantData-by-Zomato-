package com.app.rachmad.restaurant

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.app.rachmad.restaurant.ItemFragment.OnListFragmentInteractionListener
import com.app.rachmad.retrofit.R

import kotlinx.android.synthetic.main.fragment_item.view.*
import com.app.rachmad.restaurant.`object`.RestaurantItemData
import com.app.rachmad.restaurant.`object`.RestaurantSingleData
import kotlin.collections.ArrayList

class MyItemRecyclerViewAdapter(
        val f: FragmentActivity,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {
    var mValues: ArrayList<RestaurantItemData> = ArrayList()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as RestaurantItemData
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    fun submitList(list: List<RestaurantSingleData>){
        for(i in 0 .. list.size - 1){
            mValues.add(list.get(i).restaurant)
            notifyItemInserted(i)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        val params = holder.main.getLayoutParams() as RecyclerView.LayoutParams
        params.setMargins(0,
                if(position == 0)
                    f.resources.getDimensionPixelSize(R.dimen.margin_normal)
                else
                    0,
                0,
                f.resources.getDimensionPixelSize(R.dimen.margin_normal))
        holder.main.setLayoutParams(params)

        GlideApp.with(holder.image)
                .load(item.featured_image)
                .centerCrop()
                .thumbnail(GlideApp.with(holder.image).load(R.drawable.loading))
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(holder.image)

        with(holder){
            title.text = item.name
            overview.text = item.location?.address?.let { it } ?: run { "" }
            rating.text = item.user_rating?.aggregate_rating
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val image = mView.image
        val title = mView.title
        val overview = mView.overview
        val rating = mView.rating
        val main = mView.main
    }
}
