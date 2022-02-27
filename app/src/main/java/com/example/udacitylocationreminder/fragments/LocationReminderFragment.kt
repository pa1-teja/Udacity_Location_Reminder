package com.example.udacitylocationreminder.fragments

import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.udacitylocationreminder.*
import com.example.udacitylocationreminder.adapters.LocationReminderRecylerListAdapter
import com.example.udacitylocationreminder.database.LocationReminderDatabase
import com.example.udacitylocationreminder.databinding.LocationReminderFragmentBinding
import com.example.udacitylocationreminder.firebase.FirebaseAuthUtils
import com.example.udacitylocationreminder.viewmodelfactories.LocationReminderViewModelFactory
import com.example.udacitylocationreminder.viewmodels.LocationReminderViewModel

class LocationReminderFragment : BaseFragment(), View.OnClickListener {


    private lateinit var permissionRequestResult: ActivityResultLauncher<Array<String>>
    private lateinit var locationReminderViewModel: LocationReminderViewModel
    private lateinit var locationReminderFragmentBinding: LocationReminderFragmentBinding

    private val adapter = LocationReminderRecylerListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locationReminderFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.location_reminder_fragment, container, false)
        locationReminderFragmentBinding.addReminderFloatBtn.setOnClickListener(this)
        locationReminderFragmentBinding.lifecycleOwner = this
        val factory = LocationReminderViewModelFactory(
            LocationReminderDatabase.getDatabaseInstance(requireContext())
        )
        locationReminderViewModel =
            ViewModelProvider(this, factory).get(LocationReminderViewModel::class.java)

        locationReminderViewModel.getListOfReminders()
        locationReminderFragmentBinding.remindersList.adapter = adapter
        locationReminderViewModel.remindersList.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                locationReminderFragmentBinding.remindersList.visibility = View.GONE
                locationReminderFragmentBinding.emptyReminderImg.visibility = View.VISIBLE
                locationReminderFragmentBinding.noDataMsg.visibility = View.VISIBLE
            } else {
                adapter.submitList(it)
                locationReminderFragmentBinding.remindersList.visibility = View.VISIBLE
                locationReminderFragmentBinding.emptyReminderImg.visibility = View.GONE
                locationReminderFragmentBinding.noDataMsg.visibility = View.GONE
            }
        })

        permissionRequestResult =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                it.entries.forEach {
                    if (!it.value) {
                        checkDeviceLocationSettingsAndStartGeofence(
                            true,
                            requireContext(),
                            locationReminderFragmentBinding.root,
                            requireActivity()
                        )
                    }
                }
            }
        permissionRequestResult.launch(permissionsArray)
        permissionRequestResult.unregister()

        if (isOSVersionBiggerThanQ()) {
            val permissionResult =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    if (!it) {
                        checkDeviceLocationSettingsAndStartGeofence(
                            true,
                            requireContext(),
                            locationReminderFragmentBinding.root,
                            requireActivity()
                        )
                    }
                }
            permissionResult.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

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
                FirebaseAuthUtils.setCurrentUser(null)
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