package com.example.udacitylocationreminder.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.udacitylocationreminder.firebase.FirebaseAuthUtils
import com.example.udacitylocationreminder.firebase.FirebaseUserLiveData
import com.google.firebase.auth.FirebaseUser

class LaunchFragmentViewModel(context: Context): ViewModel() {

     val firebaseUser = FirebaseUserLiveData()

}