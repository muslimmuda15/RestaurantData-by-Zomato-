package com.app.rachmad.restaurant

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.rachmad.restaurant.`object`.RestaurantItemData
import com.app.rachmad.restaurant.viewmodel.ListModel
import com.app.rachmad.restaurant.R
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.fragment_item_list.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ItemFragment.OnListFragmentInteractionListener] interface.
 */
class ItemFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    private fun loadData(view: View, adapter: MyItemRecyclerViewAdapter, isRefreshing: Boolean){
        val viewModel = ViewModelProviders.of(this).get(ListModel::class.java)

        val connection = viewModel.getConnectionLive()
        viewModel.restaurant()

        // Set the adapter
        if (view.list is RecyclerView) {
            connection.observe(this, Observer<Int> {
                when(it) {
                    -1 -> {
                        Log.d("data", "ERROR => " + viewModel.getError())
                        loading.visibility = View.GONE
                        list.visibility = ViewGroup.GONE
                        error.visibility = ViewGroup.VISIBLE
                        error_text.text = "Error: Cannot get data, make sure you are connect the internet and try again!"
                        if(isRefreshing){
                            refresh_layout.isRefreshing = false
                        }
                    }
                    0 -> {
                        Log.d("data", "LOADING")
                        loading.visibility = View.VISIBLE
                        list.visibility = ViewGroup.GONE
                        error.visibility = ViewGroup.GONE
                    }
                    1 -> {
                        val listData = viewModel.getMovieList()
                        if(listData.size > 0) {
                            Log.d("data", "SUCCESS")
                            loading.visibility = View.GONE
                            list.visibility = ViewGroup.VISIBLE
                            error.visibility = ViewGroup.GONE
                            adapter.submitList(listData)
                        }
                        else{
                            Log.d("data", "ERROR => ")
                            loading.visibility = View.GONE
                            list.visibility = ViewGroup.GONE
                            error.visibility = ViewGroup.VISIBLE
                            error_text.text = "Something wrong, we cannot get the data, please refresh!"
                        }
                        if (isRefreshing) {
                            refresh_layout.isRefreshing = false
                        }
                    }
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        val adapter = MyItemRecyclerViewAdapter(activity!!, listener)
        view.list.layoutManager = LinearLayoutManager(this.context)
        view.list.adapter = adapter

        loadData(view, adapter, false)

        view.refresh_layout.setOnRefreshListener {
            loadData(view, adapter, true)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: RestaurantItemData)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                ItemFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
