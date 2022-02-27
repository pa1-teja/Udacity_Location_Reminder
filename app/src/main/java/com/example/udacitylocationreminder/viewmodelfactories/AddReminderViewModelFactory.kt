package com.example.udacitylocationreminder.viewmodelfactories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.udacitylocationreminder.database.LocationReminderDatabase
import com.example.udacitylocationreminder.viewmodels.AddReminderViewModel

class AddReminderViewModelFactory(
    private val context: Context,
    private val locationReminderDatabase: LocationReminderDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddReminderViewModel::class.java)) {
            return AddReminderViewModel(context, locationReminderDatabase) as T
        } else {
            throw IllegalArgumentException("Unknown View Model class")
        }
    }
}