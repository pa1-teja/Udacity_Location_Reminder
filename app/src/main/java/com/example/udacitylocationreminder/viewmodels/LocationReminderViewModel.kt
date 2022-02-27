package com.example.udacitylocationreminder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udacitylocationreminder.database.LocationReminderDatabase
import com.example.udacitylocationreminder.database.entities.ReminderTableEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class LocationReminderViewModel(val locationReminderDatabase: LocationReminderDatabase) :
    ViewModel() {

    private val _remindersList = MutableLiveData<List<ReminderTableEntity>>()
    val remindersList: LiveData<List<ReminderTableEntity>> get() = _remindersList

    fun getListOfReminders() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                locationReminderDatabase.reminderTableDAO.getAllLocationReminder().collectLatest {
                    _remindersList.postValue(it)
                }
            }
        } catch (exception: Exception) {
            Timber.e("Error getting data from database : ${exception.message}")
        }
    }

}