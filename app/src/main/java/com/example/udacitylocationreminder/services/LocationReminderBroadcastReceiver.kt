package com.example.udacitylocationreminder.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.udacitylocationreminder.GeofenceErrorMessages
import com.example.udacitylocationreminder.showNotification
import com.google.android.gms.location.GeofencingEvent
import timber.log.Timber

class LocationReminderBroadcastReceiver : BroadcastReceiver() {



    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        when (intent.action) {
            "Geofence_Reminder_Action" -> {
                val geofenceEvent = GeofencingEvent.fromIntent(intent)
                if (geofenceEvent.hasError()) {
                    showNotification(context, "The geofence trigger has failed")
                    Timber.e(
                        "Geofence receiver error ${
                            GeofenceErrorMessages.getErrorString(
                                context,
                                geofenceEvent.errorCode
                            )
                        }"
                    )
                } else {
                    showNotification(context, "The geofence trigger has worked")
                    Timber.d("Geofence receiver success event ${geofenceEvent.triggeringLocation}")
                }
            }
            "Location_Reminder_Action" -> {
                val foregroundLocationObj = Intent("LOCATION_OBJECT")
                foregroundLocationObj.putExtra(
                    "LOCATION_OBJECT",
                    intent.extras?.get("LOCATION_OBJECT") as Location
                )
                LocalBroadcastManager.getInstance(context).sendBroadcast(foregroundLocationObj)
                Timber.d("intent.action : ${intent.action}")
                Timber.d("current location coordinates ${intent.extras?.get("LOCATION_OBJECT")}")
            }
        }

    }

    override fun peekService(myContext: Context?, service: Intent?): IBinder {
        Timber.d("peek service triggered")
        return super.peekService(myContext, service)
    }
}