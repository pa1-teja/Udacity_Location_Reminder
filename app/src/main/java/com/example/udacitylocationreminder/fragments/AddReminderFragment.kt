package com.example.udacitylocationreminder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.udacitylocationreminder.BaseFragment
import com.example.udacitylocationreminder.R
import com.example.udacitylocationreminder.database.LocationReminderDatabase
import com.example.udacitylocationreminder.database.basicReminderInfo
import com.example.udacitylocationreminder.database.entities.ReminderTableEntity
import com.example.udacitylocationreminder.databinding.FragmentAddReminderBinding
import com.example.udacitylocationreminder.viewmodelfactories.AddReminderViewModelFactory
import com.example.udacitylocationreminder.viewmodels.AddReminderViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [AddReminderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddReminderFragment : BaseFragment(), View.OnClickListener {

    private lateinit var addReminderBinding: FragmentAddReminderBinding
    private lateinit var basicReminderInfo: basicReminderInfo
    private lateinit var locationReminderDatabase: LocationReminderDatabase
    private lateinit var addReminderViewModel: AddReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        addReminderBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_reminder, container, false)
        basicReminderInfo = AddReminderFragmentArgs.fromBundle(requireArguments()).basicReminderInfo
        locationReminderDatabase = LocationReminderDatabase.getDatabaseInstance(requireContext())
        val factory = AddReminderViewModelFactory(requireContext(), locationReminderDatabase)
        addReminderViewModel =
            ViewModelProvider(this, factory).get(AddReminderViewModel::class.java)

        addReminderBinding.saveBtn.setOnClickListener(this)
        if (!basicReminderInfo.address.isBlank()) {
            addReminderBinding.locationAddress.text = basicReminderInfo.address
        }

        addReminderViewModel.isDataInserted.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(requireContext(), "Data insertion successfull", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Data insertion failed", Toast.LENGTH_SHORT).show()
            }
        })

        return addReminderBinding.root
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            addReminderBinding.saveBtn.id -> {
                if (addReminderBinding.reminderTitle.text!!.isNotEmpty() and addReminderBinding.reminderDescription.text!!.isNotEmpty()) {

                    addReminderViewModel.storeReminderDataInDatabase(
                        ReminderTableEntity(
                            reminderTitle = addReminderBinding.reminderTitle.text.toString(),
                            reminderDescription = addReminderBinding.reminderDescription.text.toString(),
                            latitude = basicReminderInfo.latLng.latitude,
                            longitude = basicReminderInfo.latLng.longitude,
                            pointOfInterestAddress = basicReminderInfo.address,
                            id = null
                        )
                    )
                    findNavController().navigate(AddReminderFragmentDirections.actionAddReminderFragmentToLocationReminderFragment())
                }
            }
        }
    }
}