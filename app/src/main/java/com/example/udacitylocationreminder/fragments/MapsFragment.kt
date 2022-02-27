package com.example.udacitylocationreminder.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.udacitylocationreminder.BaseFragment
import com.example.udacitylocationreminder.R
import com.example.udacitylocationreminder.databinding.FragmentMapsBinding
import com.example.udacitylocationreminder.viewmodelfactories.MapsFragmentViewModelFactory
import com.example.udacitylocationreminder.viewmodels.MapsFragmentViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

class MapsFragment : BaseFragment(), GoogleMap.OnMapLongClickListener, OnMapReadyCallback,
    View.OnClickListener {

    private lateinit var mapsFragmentBinding: FragmentMapsBinding

    private lateinit var mapsViewModel: MapsFragmentViewModel

    private lateinit var googleMapObj: GoogleMap

    override fun onStart() {
        super.onStart()
        context?.bindService(
            mapsViewModel.serviceIntent,
            mapsViewModel.serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        val mapsViewModelFactory = MapsFragmentViewModelFactory(requireContext())
        mapsViewModel = ViewModelProvider(
            requireActivity(),
            mapsViewModelFactory
        ).get(MapsFragmentViewModel::class.java)
        mapsFragmentBinding.viewModel = mapsViewModel
        val mapFragment =
            childFragmentManager.findFragmentById(mapsFragmentBinding.map.id) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        mapsFragmentBinding.saveMarker.setOnClickListener(this)
        mapsFragmentBinding.lifecycleOwner = this
        return mapsFragmentBinding.root
    }

    override fun onStop() {
        super.onStop()
        context?.unbindService(mapsViewModel.serviceConnection)
    }

    override fun onMapLongClick(p0: LatLng) {
        mapsViewModel.setPointOfAddressMarker(p0, googleMapObj)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapObj = googleMap
        mapsViewModel.setCurrentLocationOnMap(googleMapObj)
        googleMap.setOnMapLongClickListener(this)
        Timber.d("on map ready")
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            mapsFragmentBinding.saveMarker.id -> {
                findNavController().navigate(
                    MapsFragmentDirections.actionMapsFragmentToAddReminderFragment(
                        mapsViewModel.reminderInfo.value!!
                    )
                )
            }
        }
    }


}