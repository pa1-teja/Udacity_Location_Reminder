package com.example.udacitylocationreminder.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udacitylocationreminder.*
import com.example.udacitylocationreminder.database.LocationReminderDatabase
import com.example.udacitylocationreminder.database.entities.ReminderTableEntity
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class AddReminderViewModel(
    val context: Context,
    val locationReminderDatabase: LocationReminderDatabase
) : ViewModel() {


    private val _isDataInserted = MutableLiveData<Boolean>()
    val isDataInserted: LiveData<Boolean> get() = _isDataInserted

    private val geofencingClient = LocationServices.getGeofencingClient(context)


    fun storeReminderDataInDatabase(reminderInfo: ReminderTableEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                locationReminderDatabase.reminderTableDAO.insertLocationReminder(reminderInfo)
                _isDataInserted.postValue(true)
                addGeoFence(reminderInfo)
            } catch (exception: Exception) {
                Timber.d("exception message : ${exception.message}")
                _isDataInserted.postValue(false)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun addGeoFence(reminderInfo: ReminderTableEntity) {
        if (isLocationAccessPermissionApproved(context)) {
            val geofence = buildGeofence(reminderInfo, 30f)
            geofencingClient.addGeofences(
                buildGeofenceRequestObj(geofence),
                geofenceIntent(context)
            ).addOnSuccessListener({
                Toast.makeText(context, "Geofences added", Toast.LENGTH_SHORT).show()
            }).addOnFailureListener({
                GeofenceErrorMessages.getErrorString(context, it)
            })
        }
    }

}

