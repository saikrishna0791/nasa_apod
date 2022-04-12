package com.gs.apod.viewmodel

import androidx.lifecycle.*
import com.gs.apod.room.dao.ApodsRepo
import com.gs.apod.room.entity.FavouriteApods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavouriteApodsViewModel(private val repository: ApodsRepo) : ViewModel() {

    // Using LiveData and caching what favourites returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val favourites: LiveData<List<FavouriteApods>> = repository.favApods.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(apod: FavouriteApods) = viewModelScope.launch {
        repository.insert(apod)
    }

    fun deleteByUrl(url: String) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(url)
    }

    fun getByUrl(url: String?) : FavouriteApods? {
        return repository.getByUrl(url)
    }

}

class FavouriteApodsViewModelFactory(private val repository: ApodsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouriteApodsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavouriteApodsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}