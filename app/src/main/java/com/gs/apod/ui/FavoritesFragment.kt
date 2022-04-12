package com.gs.apod.ui

import android.os.Bundle
import android.util.Log.e
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gs.apod.ApodApplication
import com.gs.apod.R
import com.gs.apod.adapter.ApodAdapter
import com.gs.apod.databinding.FragmentFavoritesBinding
import com.gs.apod.utils.Utils
import com.gs.apod.viewmodel.FavouriteApodsViewModel
import com.gs.apod.viewmodel.FavouriteApodsViewModelFactory
import com.gs.apod.viewmodel.SharedViewModel


/**
 *
 */
class FavoritesFragment : Fragment() {
    lateinit var binding : FragmentFavoritesBinding
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val favApodsViewModel: FavouriteApodsViewModel by viewModels {
        FavouriteApodsViewModelFactory(ApodApplication.instance.repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        getDataFun()
        return binding.root
    }

    fun initRv() {
        binding.rvFavs.apply {
            this.setHasFixedSize(true) // Since we are fetching only 10 items on launch, this makes things easily optimised for recyclerView.Not a mandate though.
            this.layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getDataFun() {
        initRv()
        favApodsViewModel.favourites.observe(this.viewLifecycleOwner, Observer { apods ->
            apods?.let { listed->
                /*listed.onEach {
                    e("@@@@", it.title)
                }*/
                var adapter = ApodAdapter(activity?.baseContext!!, listed)
                binding.rvFavs.adapter = adapter
                adapter.setData(listed)

            }
        })
        if(Utils.isOnline(activity?.baseContext!!)){
            sharedViewModel.noInternet.value = false
        }
    }


}