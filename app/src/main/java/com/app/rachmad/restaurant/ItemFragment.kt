package com.app.rachmad.restaurant

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.rachmad.restaurant.`object`.RestaurantItemData
import com.app.rachmad.restaurant.viewmodel.ListModel
import com.app.rachmad.retrofit.R
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        val viewModel = ViewModelProviders.of(this).get(ListModel::class.java)

        val connection = viewModel.getConnectionLive()
        val errorText = viewModel.getError()
        viewModel.restaurant()

        val adapter = MyItemRecyclerViewAdapter(activity!!, listener)
        view.list.layoutManager = LinearLayoutManager(this.context)
        view.list.adapter = adapter

        // Set the adapter
        if (view.list is RecyclerView) {
            connection.observe(this, Observer<Int> {
                when(it) {
                    -1 -> {
                        Log.d("data", "ERROR => " + viewModel.getError())
                        loading.visibility = View.GONE
                        list.visibility = ViewGroup.GONE
                        error.visibility = ViewGroup.VISIBLE
                        error_text.text = errorText
                    }
                    0 -> {
                        Log.d("data", "LOADING")
                        loading.visibility = View.VISIBLE
                        list.visibility = ViewGroup.GONE
                        error.visibility = ViewGroup.GONE
                    }
                    1 -> {
                        Log.d("data", "SUCCESS")
                        loading.visibility = View.GONE
                        list.visibility = ViewGroup.VISIBLE
                        error.visibility = ViewGroup.GONE
                        adapter.submitList(viewModel.getMovieList())
                    }
                }
            })
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
