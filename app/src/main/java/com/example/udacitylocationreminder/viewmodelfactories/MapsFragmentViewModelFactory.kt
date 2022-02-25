package com.example.udacitylocationreminder.viewmodelfactories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.udacitylocationreminder.viewmodels.MapsFragmentViewModel
import java.lang.IllegalArgumentException

class MapsFragmentViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsFragmentViewModel::class.java)){
            return MapsFragmentViewModel(context) as T
        } else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}