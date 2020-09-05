package com.example.mapsfragment.fragments

import com.google.android.gms.maps.model.LatLng

interface MapFragmentListener {
    fun onMapClick(ltlg: LatLng)

}