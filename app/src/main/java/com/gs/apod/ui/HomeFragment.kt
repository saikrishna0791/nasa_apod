package com.gs.apod.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gs.apod.R
import com.gs.apod.adapter.ApodAdapter
import com.gs.apod.databinding.FragmentHomeBinding
import com.gs.apod.utils.Utils
import com.gs.apod.viewmodel.SharedViewModel

/**
 * Gets the 10 random APODs from NASA. This is called once per app launch. Wanted to start with a basic MVVM thingy
 */
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater);
        getDataFun();
        return binding.root
    }

    fun initRv(){
         binding.rvApod.apply {
             this.setHasFixedSize(true) // Since we are fetching only 10 items on launch, this makes things easily optimised for recyclerView.Not a mandate though.
             this.layoutManager = LinearLayoutManager(activity)
    }
}

    private fun getDataFun() {
        initRv()
        sharedViewModel.apodItem.observe(viewLifecycleOwner, { list->
            sharedViewModel.noInternet.value = false
            binding.rvApod.isVisible = true
            binding.noInternetInHomeFrag.noInternetMain.isVisible = false
            var adapter = ApodAdapter(activity?.baseContext!!, sharedViewModel.apodItem?.value!!)
            binding.rvApod.adapter = adapter
            adapter.setData(list) //Instead, mamaged the same using a diffUtils class. So no more notifyDataSetChanged here
//          adapter.notifyDataSetChanged()
        })
        if(Utils.isOnline(activity?.baseContext!!)) {
            sharedViewModel.noInternet.value = false
        }


        /*if(Utils.isOnline(activity?.baseContext!!)){
            sharedViewModel.noInternet.value = false
            binding.rvApod.isVisible = true
            binding.noInternetInHomeFrag.noInternetMain.isVisible = false

        }else{
            binding.rvApod.isVisible = false
            binding.noInternetInHomeFrag.noInternetMain.isVisible = true
            binding.noInternetInHomeFrag.retryBtn.setOnClickListener {
                if(Utils.isOnline(activity?.baseContext!!)) {
                    sharedViewModel.getApodData()
                    sharedViewModel.getApodDataByDate(Utils.getCurrentDate(null))
                    getDataFun()
                }
            }
        }*/

        sharedViewModel.noInternet.observe(viewLifecycleOwner, Observer { noInternet ->
            if(noInternet){
                binding.rvApod.isVisible = false
                binding.noInternetInHomeFrag.noInternetMain.isVisible = true
                binding.noInternetInHomeFrag.retryBtn.setOnClickListener {
                    if(Utils.isOnline(activity?.baseContext!!)) {
                        sharedViewModel.getApodData()
                        sharedViewModel.getApodDataByDate(Utils.getCurrentDate(null))
                        getDataFun()
                    }
                }
            }else{
//                sharedViewModel.noInternet.value = false
                binding.rvApod.isVisible = true
                binding.noInternetInHomeFrag.noInternetMain.isVisible = false
            }
        })
    }
}