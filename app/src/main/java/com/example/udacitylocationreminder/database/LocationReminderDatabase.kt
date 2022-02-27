package com.example.udacitylocationreminder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.udacitylocationreminder.database.daos.ReminderTableDAO
import com.example.udacitylocationreminder.database.entities.ReminderTableEntity

@Database(entities = [ReminderTableEntity::class], version = 1, exportSchema = false)
abstract class LocationReminderDatabase: RoomDatabase() {

    abstract val reminderTableDAO: ReminderTableDAO

    companion object {
        private var INSTANCE: LocationReminderDatabase? = null

        fun getDatabaseInstance(context: Context): LocationReminderDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        LocationReminderDatabase::class.java,
                        "Location_reminder_database"
                    )
                        .fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE!!
        }
    }
}