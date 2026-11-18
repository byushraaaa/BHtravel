package com.bhtravel.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bhtravel.app.data.dao.TripDao
import com.bhtravel.app.data.entity.TripEntity

@Database(
    entities = [TripEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TripDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao
}
