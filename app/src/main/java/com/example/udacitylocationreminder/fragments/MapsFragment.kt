package com.example.udacitylocationreminder.fragments

import android.content.*

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.udacitylocationreminder.BaseFragment
import com.example.udacitylocationreminder.R
import com.example.udacitylocationreminder.databinding.FragmentMapsBinding
import com.example.udacitylocationreminder.viewmodelfactories.MapsFragmentViewModelFactory
import com.example.udacitylocationreminder.viewmodels.MapsFragmentViewModel
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber

class MapsFragment() : BaseFragment(),GoogleMap.OnMapLongClickListener, OnMapReadyCallback{

    private lateinit var mapsFragmentBinding: FragmentMapsBinding
    private lateinit var googleMapObj: GoogleMap

    private lateinit var mapsViewModel: MapsFragmentViewModel

    constructor(parcel: Parcel) : this() {

    }
//    by lazy {
//        Timber.d("view model is ready")
//
//
//    }


    override fun onStart() {
        super.onStart()
        context?.bindService( mapsViewModel.serviceIntent, mapsViewModel.serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapsFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_maps,container,false)
        val mapsViewModelFactory = MapsFragmentViewModelFactory(requireActivity())
        mapsViewModel = ViewModelProvider(requireActivity(),mapsViewModelFactory).get(MapsFragmentViewModel::class.java)
        mapsFragmentBinding.lifecycleOwner = this
        mapsFragmentBinding.viewModel = mapsViewModel
        val mapFragment = childFragmentManager.findFragmentById(mapsFragmentBinding.map.id) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        mapsViewModel.location.observe(viewLifecycleOwner, Observer {
            Timber.d("current location  on fragment ${it}")
            mapsViewModel.setCurrentLocationOnMap(googleMapObj)
        })
        return mapsFragmentBinding.root
    }

    override fun onStop() {
        super.onStop()
        context?.unbindService(mapsViewModel.serviceConnection)
    }

    override fun onMapLongClick(p0: LatLng) {
        TODO("Not yet implemented")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMapObj = googleMap
        Timber.d("on map ready")
    }
}