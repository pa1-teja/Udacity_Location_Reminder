package com.example.udacitylocationreminder.viewmodelfactories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.udacitylocationreminder.viewmodels.LaunchFragmentViewModel
import java.lang.IllegalArgumentException


class LaunchFragmentViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LaunchFragmentViewModel::class.java)){
            return LaunchFragmentViewModel(context) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}