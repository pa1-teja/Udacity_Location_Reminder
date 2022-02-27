package com.example.udacitylocationreminder.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udacitylocationreminder.database.LocationReminderDatabase
import com.example.udacitylocationreminder.database.entities.ReminderTableEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class AddReminderViewModel(
    val context: Context,
    val locationReminderDatabase: LocationReminderDatabase
) : ViewModel() {


    private val _isDataInserted = MutableLiveData<Boolean>()
    val isDataInserted: LiveData<Boolean> get() = _isDataInserted

    fun storeReminderDataInDatabase(reminderInfo: ReminderTableEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                locationReminderDatabase.reminderTableDAO.insertLocationReminder(reminderInfo)
                _isDataInserted.postValue(true)
            } catch (exception: Exception) {
                Timber.d("exception message : ${exception.message}")
                _isDataInserted.postValue(false)
            }
        }
    }
}
