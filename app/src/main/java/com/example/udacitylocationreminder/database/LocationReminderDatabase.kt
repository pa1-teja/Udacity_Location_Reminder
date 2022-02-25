package com.example.udacitylocationreminder.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class LocationReminderDatabase: RoomDatabase() {

    companion object{
        private var INSTANCE: LocationReminderDatabase? = null

        fun getDatabaseInstance(context: Context): LocationReminderDatabase{
            synchronized(this){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context,LocationReminderDatabase::class.java,"Location_reminder_database")
                        .fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE!!
        }
    }
}