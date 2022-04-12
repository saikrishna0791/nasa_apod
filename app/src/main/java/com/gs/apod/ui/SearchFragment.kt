package com.gs.apod.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gs.apod.databinding.FragmentSearchBinding

import android.app.DatePickerDialog
import android.util.Log.e
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gs.apod.ApodApplication
import com.gs.apod.R
import com.gs.apod.adapter.FavouriteListener
import com.gs.apod.room.entity.FavouriteApods
import com.gs.apod.utils.Utils
import com.gs.apod.viewmodel.FavouriteApodsViewModel
import com.gs.apod.viewmodel.FavouriteApodsViewModelFactory
import com.gs.apod.viewmodel.SharedViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.Dispatcher
import java.util.*


/**
 *
 */
class SearchFragment : Fragment(), FavouriteListener {

    lateinit var binding : FragmentSearchBinding
    lateinit var dateString : String
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val favApodsViewModel: FavouriteApodsViewModel by viewModels {
        FavouriteApodsViewModelFactory(ApodApplication.instance.repository)
    }
    var options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.progress_anim)
        .error(android.R.drawable.stat_notify_error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater);
        binding.dateTv.text = sharedViewModel.lastFetchText
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_search, container, false)
        if(Utils.isOnline(activity?.baseContext!!)){
            sharedViewModel.noInternet.value = false
        }
        initCalendar()
//        sharedViewModel.getApodDataByDate(Utils.getCurrentDate(null))
        sharedViewModel.apodItemByDate.observe(viewLifecycleOwner, { data->
            if(binding.pbSearch.isVisible){
                binding.pbSearch.isVisible = false
            }
            binding.searchCard.isVisible = true
            binding.cardTxt.text = data.title.trim()
            binding.secondTxt.text = data.explanation?.trim()
            Glide.with(activity?.baseContext!!)
                .load(data.hdurl)
                .apply(options)
                .into(binding.apodIv);
            var apod : FavouriteApods? = favApodsViewModel.getByUrl(data.url)
            if(apod != null){
                data.isLiked = true
            }
            binding.favLv.apply {
                if (apod!=null && data.isLiked) {
                    playAnimation()
                }else{
                    if(isAnimating){
                        progress = 0.0f
                        cancelAnimation()
                        clearAnimation()
                    }

                }
            }
            binding.favLv.setOnClickListener {
                data.isLiked = !data.isLiked
                binding.favLv.apply {
                    if (data.isLiked) {
                        playAnimation()
                        favApodsViewModel.insert(data)
                    } else {
                        if(isAnimating){
                            cancelAnimation()
                            clearAnimation()
                        }
                        try{
                            favApodsViewModel.deleteByUrl(data.url)
                        }catch(e: Exception){
//                            e("@@@", "Delete has exception " + e.message)
                        }
                        progress = 0.0f
                    }
                }
            }
        })
        return binding.root
    }

    private fun initCalendar() {
        binding.calendarImage.setOnClickListener {
            val datePickerDialog = activity?.let { it1 ->
                val c = Calendar.getInstance()
                val mYear = c[Calendar.YEAR]
                val mMonth = c[Calendar.MONTH]
                val mDay = c[Calendar.DAY_OF_MONTH]
                DatePickerDialog(
                    it1,
                    { datePicker, year, month, day ->
                        binding.dateTv.text = "$year-${month+1}-$day's APOD"
                        dateString = "$year-${month+1}-$day"
                        sharedViewModel.lastFetchText = binding.dateTv.text.toString()
                        if(Utils.isOnline(activity?.baseContext!!)){
                            sharedViewModel.noInternet.value = false
                            binding.pbSearch.isVisible = true
                        }else{
                            sharedViewModel.noInternet.value = true
                            binding.pbSearch.isVisible = false
                        }
                        sharedViewModel.getApodDataByDate(Utils.getCurrentDate(dateString))
                    }, mYear, mMonth, mDay
                )
            }

            var cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -1);
            datePickerDialog?.datePicker?.maxDate = cal.timeInMillis //set max date as yesterday as the API is on US time I guess
            if(datePickerDialog?.isShowing == false){
                datePickerDialog?.show()
            }
        }
    }

    override fun addTofavourite(data: FavouriteApods) {
        TODO("Not yet implemented")
    }
}