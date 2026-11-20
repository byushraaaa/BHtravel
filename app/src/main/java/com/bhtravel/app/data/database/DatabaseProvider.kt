
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
            )
                .addMigrations(
                    TripDatabase.MIGRATION_1_2,
                    TripDatabase.MIGRATION_2_3
                )                .build()

            INSTANCE = instance
            instance
        }
    }
}
