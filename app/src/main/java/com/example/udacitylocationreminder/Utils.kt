package com.example.udacitylocationreminder

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentActivity
import com.example.udacitylocationreminder.database.entities.ReminderTableEntity
import com.example.udacitylocationreminder.services.LocationReminderBroadcastReceiver
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


val permissionsArray = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_COARSE_LOCATION
)


fun isOSVersionBiggerThanQ(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}


fun createNotificationChannel(context: Context) {
    val channelName = context.getString(R.string.channel_name)
    val channelId = context.getString(R.string.channel_id)
    val notificationDescription = context.getString(R.string.notification_description)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
            description = notificationDescription
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

private fun getNotificationBuilder(
    context: Context,
    notificationText: String
): NotificationCompat.Builder {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val autoCancel = NotificationCompat.Builder(context, context.getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(false)
        autoCancel
    } else {
        NotificationCompat.Builder(context, context.getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
            .setContentTitle(R.string.notification_title.toString())
            .setContentText(R.string.notification_description.toString())
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(false)

    }

}


fun showNotification(context: Context, notificationMessage: String) {
    NotificationManagerCompat.from(context).notify(
        context.getString(R.string.channel_id).toInt(),
        getNotificationBuilder(context, notificationMessage).build()
    )
}

fun isLocationAccessPermissionApproved(context: Context): Boolean {
    val foregroundPermissionApproved = (PackageManager.PERMISSION_GRANTED.equals(
        ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    ))
    val backgroundPermissionApproved = if (isOSVersionBiggerThanQ()) {
        (PackageManager.PERMISSION_GRANTED.equals(
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        ))
    } else {
        true
    }
    return foregroundPermissionApproved and backgroundPermissionApproved
}

 fun checkDeviceLocationSettingsAndStartGeofence(resolve:Boolean = true, context: Context,view:View, activity: FragmentActivity){
    val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
        priority = com.google.android.gms.location.LocationRequest.PRIORITY_LOW_POWER
    }

    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val settingsClient = LocationServices.getSettingsClient(context)
    val locationSettingsResponseTask = settingsClient.checkLocationSettings(builder.build())
    locationSettingsResponseTask.addOnFailureListener{ exception ->
        if (exception is ResolvableApiException && resolve){
            try {
                exception.startResolutionForResult(activity,
                    REQUEST_TURN_DEVICE_LOCATION_ON
                )
            }catch (exception: IntentSender.SendIntentException){
                Timber.e("Error getting location settings resolution: ${exception.message}")
            }
        }else{
            Snackbar.make(view,
                R.string.location_required_error, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok){
//                    checkDeviceLocationSettingsAndStartGeofence()
                }.show()
        }
    }

     locationSettingsResponseTask.addOnCompleteListener {
         if (it.isSuccessful) {
             //TODO add geofence
         }
     }
 }

@SuppressLint("UnspecifiedImmutableFlag")
fun geofenceIntent(context: Context): PendingIntent {
    val intent = Intent(context, LocationReminderBroadcastReceiver::class.java).apply {
        action = "Geofence_Reminder_Action"
    }
    context.registerReceiver(
        LocationReminderBroadcastReceiver(),
        IntentFilter("Geofence_Reminder_Action")
    )
    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}


fun buildGeofence(reminderInfo: ReminderTableEntity, radius: Float): Geofence {
    return Geofence.Builder().setRequestId(reminderInfo.id.toString()).setCircularRegion(
        reminderInfo.latitude,
        reminderInfo.longitude,
        radius
    )
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
        .setExpirationDuration(Geofence.NEVER_EXPIRE)
        .build()
}


fun buildGeofenceRequestObj(geofence: Geofence): GeofencingRequest {
    return GeofencingRequest.Builder().setInitialTrigger(0).addGeofence(geofence).build()
}

object GeofenceErrorMessages {
    fun getErrorString(context: Context, e: Exception): String {
        return if (e is ApiException) {
            getErrorString(context, e.statusCode)
        } else {
            context.resources.getString(R.string.geofence_unknown_error)
        }
    }

    fun getErrorString(context: Context, errorCode: Int): String {
        val resources = context.resources
        return when (errorCode) {
            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE ->
                resources.getString(R.string.geofence_not_available)

            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES ->
                resources.getString(R.string.geofence_too_many_geofences)

            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS ->
                resources.getString(R.string.geofence_too_many_pending_intents)

            else -> resources.getString(R.string.geofence_unknown_error)
        }
    }
}

private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29


