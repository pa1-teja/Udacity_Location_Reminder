package com.example.udacitylocationreminder.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.udacitylocationreminder.database.LocationReminderDatabase
import com.example.udacitylocationreminder.viewmodels.LocationReminderViewModel

class LocationReminderViewModelFactory(private val locationReminderDatabase: LocationReminderDatabase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationReminderViewModel::class.java)) {
            return LocationReminderViewModel(locationReminderDatabase) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}