package com.example.udacitylocationreminder.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.udacitylocationreminder.viewmodels.MapsFragmentViewModel
import com.example.udacitylocationreminder.viewmodels.MapsFragmentViewModel.locationReceiver
import timber.log.Timber

class LocationReminderBroadcastReceiver : BroadcastReceiver() {



    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        LocalBroadcastManager.getInstance(context).registerReceiver(
             MapsFragmentViewModel(context).locationReceiver(),
            IntentFilter("LOCATION_OBJECT")
        )
        val foregroundLocationObj = Intent("LOCATION_OBJECT")
        foregroundLocationObj.putExtra("LOCATION_OBJECT", intent.extras?.get("LOCATION_OBJECT") as Location)
        LocalBroadcastManager.getInstance(context).sendBroadcast(foregroundLocationObj)

        Timber.d("intent.action : ${intent.action}")
        Timber.d("current location coordinates ${intent.extras?.get("LOCATION_OBJECT")}")
    }

    override fun peekService(myContext: Context?, service: Intent?): IBinder {
        Timber.d("peek service triggered")
        return super.peekService(myContext, service)
    }
}