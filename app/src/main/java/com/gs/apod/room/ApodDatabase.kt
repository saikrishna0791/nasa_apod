package com.gs.apod.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gs.apod.room.dao.ApodDao
import com.gs.apod.room.entity.FavouriteApods
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        FavouriteApods::class
    ],
    version = 1
)
abstract class ApodDatabase : RoomDatabase(){
    abstract val apodDao: ApodDao

    private class ApodDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
//                    var apodDao = database.apodDao

                    // Delete all content here.
//                    apodDao.deleteAll()

//                    var apod = FavouriteApods("Hello", "2022-04-03", "dummy", "https://apod.nasa.gov/apod/image/1707/TOA1-M8M20-SL-DCP01andB600-09-Final7-Cc1024.jpg","image", "v1", "DummyTitle", "https://apod.nasa.gov/apod/image/1707/TOA1-M8M20-SL-DCP01andB600-09-Final7-Cc1024.jpg")
//                    apodDao.insertFavApod(apod)
                }
            }
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: ApodDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ApodDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApodDatabase::class.java,
                    "apod_database"
                )
                    .addCallback(ApodDatabaseCallback(scope))
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}