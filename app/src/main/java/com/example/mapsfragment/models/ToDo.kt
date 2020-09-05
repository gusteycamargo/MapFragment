package com.example.mapsfragment.models

import com.google.android.gms.maps.model.LatLng

data class ToDo(
    var status: String,
    var title: String,
    var description: String,
    var latLng: LatLng? ) {

    var id: Int? = null
}