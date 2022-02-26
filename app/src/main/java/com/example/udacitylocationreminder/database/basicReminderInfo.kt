package com.example.udacitylocationreminder.database

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class basicReminderInfo(val address: String, val latLng: LatLng) : Parcelable
