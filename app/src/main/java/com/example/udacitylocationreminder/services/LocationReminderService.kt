package com.example.udacitylocationreminder.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LocationReminderService: Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null


    /*
     * Checks whether the bound activity has really gone away (foreground service with notification
     * created) or simply orientation change (no-op).
     */
    private var configurationChange = false

    private var serviceRunningInForeground = false

    private val localBinder = LocalBinder()

    private lateinit var notificationManager: NotificationManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Location service started")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val intentFilter = IntentFilter("Location_Reminder_Action")
        val intent = Intent()
        intent.action = "Location_Reminder_Action"

        registerReceiver(LocationReminderBroadcastReceiver(),intentFilter)

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(5)
            fastestInterval = TimeUnit.SECONDS.toMillis(2)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                Timber.d("onLocation result called :  ${p0.lastLocation}")
                currentLocation = p0.lastLocation

                intent.putExtra("LOCATION_OBJECT",currentLocation)
                sendBroadcast(intent)

            }
        }
    }

    override fun onBind(p0: Intent?): IBinder {
        Timber.d("onBind()")

        // MainActivity (client) comes into foreground and binds to service, so the service can
        // become a background services.
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        return localBinder
    }

    override fun onRebind(intent: Intent?) {
        Timber.d( "onRebind()")

        // MainActivity (client) returns to the foreground and rebinds to service, so the service
        // can become a background services.
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(LocationReminderBroadcastReceiver())
        Timber.e("${packageName} service is destroyed")
    }

    inner class LocalBinder: Binder(){
        internal val service: LocationReminderService get() = this@LocationReminderService
    }

//    @SuppressLint("MissingPermission")
    fun subscribeToLocationUpdates(){
        Timber.d("subscribeToLocationUpdates()")
//        startService(Intent(applicationContext,LocationReminderService::class.java))
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
        }catch (exception: SecurityException){
            Timber.e("Lost location permissions. Couldn't remove updates: ${exception.message}")
        }
    }

    fun unsubscribeToLocationUpdates() {
        Timber.d("unsubscribeToLocationUpdates()")

        try {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Location Callback removed.")
                    stopSelf()
                } else {
                    Timber.d("Failed to remove Location Callback.")
                }
            }

        } catch (unlikely: SecurityException) {
            Timber.e("Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }
}