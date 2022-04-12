package com.gs.apod.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.*
import com.gs.apod.R
import com.gs.apod.model.ApodItem
import com.gs.apod.network.RetrofitInstance
import com.gs.apod.room.ApodDatabase
import com.gs.apod.room.dao.ApodDao
import com.gs.apod.room.entity.FavouriteApods
import com.gs.apod.utils.Utils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private var _apodItems = MutableLiveData<List<FavouriteApods>>()
    private var _apodItemByDate = MutableLiveData<FavouriteApods>()
    var apodItem : LiveData<List<FavouriteApods>> = _apodItems
    var apodItemByDate : LiveData<FavouriteApods> = _apodItemByDate
    var noInternet : MutableLiveData<Boolean> = MutableLiveData()
//    var dao: ApodDao? = null

    var selectedFragId : Int = R.id.nav_home
    var lastFetchText : String = "Latest one is here"

    init {
        noInternet.value = false
        getApodData()
        getApodDataByDate(Utils.getCurrentDate(null))
//        dao = ApodDatabase.getInstance(getApplication()).apoDao
    }


    fun setApodItems(apodItems : List<FavouriteApods>){
        _apodItems.value = apodItems
    }

    fun setApodItemByDate(apodItem : FavouriteApods){
        _apodItemByDate.value = apodItem
    }

    fun getApodData()  {
        viewModelScope.launch {
            try{
                val response = RetrofitInstance.api.getApodData()
                if(response.isSuccessful && response.body()!!.isNotEmpty()){
                    setApodItems(response.body()!!)
                }
            }catch(e: IOException){
                if(noInternet.value == false){
//                    Toast.makeText(getApplication(), "No internet Connection", Toast.LENGTH_LONG).show()
                    noInternet.value = true
                }

            }
        }
    }

    fun getApodDataByDate(date : String?)  {
        viewModelScope.launch {
            try{
                val response = RetrofitInstance.api.getSingleApodData(Utils.getCurrentDate(date))
                if(response.isSuccessful && response.body()!=null){
                    setApodItemByDate(response.body()!!)
                }
            }catch(e: IOException){
                if(noInternet.value == false){
//                    Toast.makeText(getApplication(), "No internet Connection", Toast.LENGTH_LONG).show()
                    noInternet.value = true
                }
            }
        }
    }

    /*fun saveToDb(data: FavouriteApods?) {
        viewModelScope.launch {
            data?.let { dao?.insertFavApod(it) }
        }
    }*/
}