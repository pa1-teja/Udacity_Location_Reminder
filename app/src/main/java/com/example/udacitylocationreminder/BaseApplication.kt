package com.example.udacitylocationreminder

import android.app.Application
import com.example.udacitylocationreminder.firebase.FirebaseAuthUtils
import timber.log.Timber

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FirebaseAuthUtils.initializeFirebaseAuth()
        createNotificationChannel(applicationContext)
    }

}