package com.example.udacitylocationreminder.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.udacitylocationreminder.*
import com.example.udacitylocationreminder.databinding.LocationReminderFragmentBinding
import com.example.udacitylocationreminder.firebase.FirebaseAuthUtils
import com.example.udacitylocationreminder.viewmodelfactories.LocationReminderViewModelFactory
import com.example.udacitylocationreminder.viewmodels.LocationReminderViewModel
import timber.log.Timber

class LocationReminderFragment : BaseFragment(), View.OnClickListener {


    private lateinit var permissionRequestResult: ActivityResultLauncher<Array<String>>
    private val locationReminderViewModel: LocationReminderViewModel by lazy {
        val factory = LocationReminderViewModelFactory(requireContext())
        ViewModelProvider(this,factory).get(LocationReminderViewModel::class.java)
    }
    private lateinit var locationReminderFragmentBinding: LocationReminderFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locationReminderFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.location_reminder_fragment,container,false)

        permissionRequestResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            it.entries.forEach{
                if (!it.value){
                    checkDeviceLocationSettingsAndStartGeofence(true,requireContext(),locationReminderFragmentBinding.root,requireActivity())
                }
            }
        }
        permissionRequestResult.launch(permissionsArray)
        permissionRequestResult.unregister()

        if (isOSVersionBiggerThanQ()){
            val permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()){
                if (!it){
                    checkDeviceLocationSettingsAndStartGeofence(true,requireContext(),locationReminderFragmentBinding.root,requireActivity())
                }
            }
            permissionResult.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }



        locationReminderFragmentBinding.addReminderFloatBtn.setOnClickListener(this)
        locationReminderFragmentBinding.lifecycleOwner = this

        setHasOptionsMenu(true)
        return locationReminderFragmentBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout_btn -> {
                FirebaseAuthUtils.signOutUser(requireContext())
                    findNavController().navigate(LocationReminderFragmentDirections.actionLocationReminderFragmentToLaunchFragment())
            }
        }
        return true
    }


    override fun onClick(p0: View?) {
       when(p0?.id){
           locationReminderFragmentBinding.addReminderFloatBtn.id -> {
               findNavController().navigate(LocationReminderFragmentDirections.actionLocationReminderFragmentToMapsFragment())
           }
       }
    }

}