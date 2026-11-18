package com.bhtravel.app.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: TripDatabase? = null

    fun getDatabase(context: Context): TripDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TripDatabase::class.java,
                "bhtravel_database"
            ).build()

            INSTANCE = instance
            instance
        }
    }
}