package com.example.udacitylocationreminder.viewmodels

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Geocoder
import android.location.Location
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.udacitylocationreminder.database.basicReminderInfo
import com.example.udacitylocationreminder.services.LocationReminderService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber

class MapsFragmentViewModel(val context: Context): ViewModel(), ServiceConnection {


    private var locationReminderServiceBound = false

    @SuppressLint("StaticFieldLeak")
    private var locationReminderService: LocationReminderService? = null

    private val _reminderInfo = MutableLiveData<basicReminderInfo>()
    val reminderInfo: LiveData<basicReminderInfo> get() = _reminderInfo

    val serviceIntent = Intent(context, LocationReminderService::class.java)
    val serviceConnection = this
    private lateinit var locationMapsObj: Location

    private var fusedLocationProviderClient = FusedLocationProviderClient(context)

    init {
        context.startService(serviceIntent)

    }


    fun setCurrentLocationOnMap(googleMap: GoogleMap) {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {

            val geocodingApi = Geocoder(context)
            val currentLocation = LatLng(it.result.latitude, it.result.longitude)
            val resolvedAddress =
                geocodingApi.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
            var addressName = ""
            resolvedAddress.forEachIndexed(action = { index, address ->
                Timber.d("location address ${address.getAddressLine(index)}")
                addressName = address.featureName
            })

            _reminderInfo.value = basicReminderInfo(addressName, currentLocation)
            googleMap.addMarker(
                MarkerOptions().position(currentLocation).title("Your current location")
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        }
    }

    fun setPointOfAddressMarker(latLng: LatLng, googleMap: GoogleMap) {

        val geocodingApi = Geocoder(context)

        val resolvedAddress = geocodingApi.getFromLocation(latLng.latitude, latLng.longitude, 1)
        var addressName = ""
        resolvedAddress.forEachIndexed(action = { index, address ->
            Timber.d("location address ${address.getAddressLine(index)}")
            addressName = address.featureName
        })

        _reminderInfo.value = basicReminderInfo(addressName, latLng)
        googleMap.addMarker(
            MarkerOptions().position(latLng).title(addressName)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
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
    }

}