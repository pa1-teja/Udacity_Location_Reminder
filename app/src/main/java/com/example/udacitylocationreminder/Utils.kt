package com.example.udacitylocationreminder

import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


val permissionsArray = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_COARSE_LOCATION)


fun isOSVersionBiggerThanQ():Boolean{
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}


 fun isLocationAccessPermissionApproved(context: Context): Boolean {
    val foregroundPermissionApproved = (PackageManager.PERMISSION_GRANTED.equals(ActivityCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_FINE_LOCATION)))
    val backgroundPermissionApproved = if(isOSVersionBiggerThanQ()){
        (PackageManager.PERMISSION_GRANTED.equals(ActivityCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)))
    }else{
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

    locationSettingsResponseTask.addOnCompleteListener{
        if (it.isSuccessful){
            //TODO add geofence
        }
    }
}


private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
    private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
    private const val LOCATION_PERMISSION_INDEX = 0
    private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
