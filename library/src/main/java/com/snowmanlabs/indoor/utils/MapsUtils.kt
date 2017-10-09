package com.snowmanlabs.indoor.utils

import com.snowmanlabs.indoor.model.Floor
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

/**
 * Created by diefferson on 09/10/17.
 **/
abstract class MapsUtils{

    companion object {

        fun markerFactory(mMap: GoogleMap, lat: Double?, lng: Double?, title: String, snippet: String): Marker {
            val latLng = LatLng(lat!!, lng!!)
            val markerOptions = MarkerOptions()
                    .position(latLng)
            val marker = mMap.addMarker(markerOptions)
            return marker
        }

        fun groundOverlayFactory(mMap: GoogleMap, floor : Floor): GroundOverlay?{

            if (mMap != null) {
                val bitmapDescriptor = floor.image!!
                val center = LatLng(floor.latitude!!, floor.longitude!!)
                val fpOverlay = GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromBitmap(floor.image))
                        .visible(false)
                        .position(center, floor.width!!, floor.height!!)
                        .bearing(floor.bearing!!)
                        .transparency(0.5f)

                var groundOverlay =  mMap.addGroundOverlay(fpOverlay)
                return groundOverlay

            }

            return null

        }

    }

    //    private fun setUpGroundOverlay(floor : Int) {
//        if (mGroudOverlay != null) {
//            mGroudOverlay!!.remove()
//        }
//
//
//        if (mMap != null) {
//            val bitmapDescriptor = BitmapDescriptorFactory
//                    .fromResource(R.drawable.planta)
//            val center = LatLng(-25.456030217451996, -49.2753406921695)
//            val fpOverlay = GroundOverlayOptions()
//                    .image(bitmapDescriptor)
//                    .position(center, 20.97941f, 55.320877f)
//                    .bearing(22.5f)
//                    .transparency(0.5f)
//            mGroudOverlay = mMap.addGroundOverlay(fpOverlay)
//        }
//    }

}