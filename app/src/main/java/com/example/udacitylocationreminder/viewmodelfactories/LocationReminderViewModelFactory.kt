package com.example.udacitylocationreminder.viewmodelfactories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.udacitylocationreminder.viewmodels.LocationReminderViewModel
import java.lang.IllegalArgumentException

class LocationReminderViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationReminderViewModel::class.java)){
            return LocationReminderViewModel(context) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}