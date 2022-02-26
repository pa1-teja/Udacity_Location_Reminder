package com.example.udacitylocationreminder.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "location_reminder_table")
data class ReminderTableEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "title")
    val reminderTitle: String,

    @ColumnInfo(name = "description")
    val reminderDescription: String,

    @ColumnInfo(name = "poi_address")
    val pointOfInterestAddress: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double
)