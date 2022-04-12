package com.gs.apod

import android.app.Application
import com.gs.apod.room.ApodDatabase
import com.gs.apod.room.dao.ApodsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ApodApplication : Application() {


    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { ApodDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ApodsRepo(database.apodDao) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: ApodApplication
            private set
    }
}

