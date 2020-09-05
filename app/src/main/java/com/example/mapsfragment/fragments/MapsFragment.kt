package com.example.mapsfragment.fragments

import android.Manifest
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mapsfragment.R
import com.example.mapsfragment.adapters.ToDoListener

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MapsFragment() : Fragment() {
    private lateinit var googleMap: GoogleMap
    lateinit var latLng: LatLng
    private val listener: MapFragmentListener?
    get() {
        if (activity is MapFragmentListener) {
            return activity as MapFragmentListener
        }
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            this.googleMap = googleMap

            googleMap.setOnMapClickListener {
                addMarker(it, "Clicado")
                latLng = it
                listener?.onMapClick(it)
            }
            googleMap.uiSettings.isZoomControlsEnabled = true
            Dexter.withContext(activity)
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .withListener(object : MultiplePermissionsListener {
                    @SuppressLint("MissingPermission")
                    override fun onPermissionsChecked(p: MultiplePermissionsReport) {
                        if (p.areAllPermissionsGranted()) {
                            googleMap.isMyLocationEnabled = true
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?, p1: PermissionToken?) { }
                }).check()

        }
    }

    private fun addMarker(position: LatLng, title: String? = null, moveCamera: Boolean? = true) {
        googleMap.clear()
        googleMap.addMarker(
            MarkerOptions().position(position).title(title)
        )
        val camera = CameraUpdateFactory.newLatLngZoom(position, 15F)
        googleMap.moveCamera(camera)
    }
}