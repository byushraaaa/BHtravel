
package com.bhtravel.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bhtravel.app.data.dao.ExpenseDao
import com.bhtravel.app.data.dao.ItineraryDao
import com.bhtravel.app.data.dao.TripDao
import com.bhtravel.app.data.entity.ExpenseEntity
import com.bhtravel.app.data.entity.ItineraryItemEntity
import com.bhtravel.app.data.entity.TripEntity

@Database(
    entities = [
        TripEntity::class,
        ExpenseEntity::class,
        ItineraryItemEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class TripDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao

    abstract fun expenseDao(): ExpenseDao

    abstract fun itineraryDao(): ItineraryDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS expenses (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        tripId INTEGER NOT NULL,
                        category TEXT NOT NULL,
                        amount REAL NOT NULL,
                        description TEXT NOT NULL,
                        date TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS itinerary_items (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        tripId INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        date TEXT NOT NULL,
                        time TEXT NOT NULL,
                        location TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}
