package com.gs.apod.room.dao

import androidx.annotation.WorkerThread
import com.gs.apod.room.entity.FavouriteApods
import kotlinx.coroutines.flow.Flow


class ApodsRepo(private val apodDao: ApodDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val favApods: Flow<List<FavouriteApods>> = apodDao.getAllFavApods()

    var favApodByUrl: FavouriteApods? = null

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(apod: FavouriteApods) {
        apodDao.insertFavApod(apod)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(url: String) {
        apodDao.deleteByUrl(url)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getByUrl(url: String?) : FavouriteApods? {
        return apodDao.getByUrl(url!!)
    }


}