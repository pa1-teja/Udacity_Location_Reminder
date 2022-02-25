package com.example.udacitylocationreminder.database.entities

import androidx.room.Entity
import com.google.android.gms.maps.model.PointOfInterest


@Entity
data class ReminderTable(

    val id:Long,

    val reminderDescription: String,

    val pointOfInterestAddress: String,

    val latitude: Double,

    val longitude: Double
)