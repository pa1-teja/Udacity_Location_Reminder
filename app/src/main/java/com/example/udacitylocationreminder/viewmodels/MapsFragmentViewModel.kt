package com.example.udacitylocationreminder.viewmodels

import android.annotation.SuppressLint
import android.content.*
import android.location.Location
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.udacitylocationreminder.services.LocationReminderService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber

class MapsFragmentViewModel(val context: Context): ViewModel(), ServiceConnection {

    private var locationReminderServiceBound = false

    @SuppressLint("StaticFieldLeak")
    private  var locationReminderService: LocationReminderService? = null

    private val _location = MutableLiveData<Location>()
    val location:LiveData<Location> get() = _location

    val serviceIntent = Intent(context, LocationReminderService::class.java)
    val serviceConnection = this

    init {
        context.startService(serviceIntent)
    }

    fun setCurrentLocationOnMap(googleMap: GoogleMap){
        if (location.value != null) {
            val currentLocation = LatLng(location.value!!.latitude, location.value!!.longitude)
            googleMap.addMarker(
                MarkerOptions().position(currentLocation).title("Your current location")
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        } else{
            Timber.e("current location not available")
        }
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as LocationReminderService.LocalBinder
        Timber.d("Location service is connected")
        locationReminderServiceBound = true
        locationReminderService = binder.service
        locationReminderService?.subscribeToLocationUpdates()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        Timber.d("Location service is disconnected")
        locationReminderService?.unsubscribeToLocationUpdates()
        locationReminderServiceBound = false
        locationReminderService = null
        locationReceiver().clearAbortBroadcast()
    }

    inner class locationReceiver: BroadcastReceiver(){

        override fun onReceive(p0: Context?, p1: Intent?) {
            _location.value = p1?.extras?.get("LOCATION_OBJECT") as Location
        }
    }
}