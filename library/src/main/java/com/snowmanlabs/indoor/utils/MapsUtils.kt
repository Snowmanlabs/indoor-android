package com.snowmanlabs.indoor.utils

import android.content.Context
import com.snowmanlabs.indoor.domain.Floor
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

         fun markerFactory(mMap: GoogleMap,lat: Double?, lng: Double?, title: String, snippet: String, icon: Int): Marker {
            val latLng = LatLng(lat!!, lng!!)
            val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromResource(icon))
            val marker = mMap.addMarker(markerOptions)
            return marker
        }

        fun groundOverlayFactory(mMap: GoogleMap, floor : Floor, bitmapDescriptor: BitmapDescriptor): GroundOverlay?{

            if (mMap != null) {
                val center = LatLng(floor.latitude!!, floor.longitude!!)
                val fpOverlay = GroundOverlayOptions()
                        .image(bitmapDescriptor)
                        .visible(false)
                        .position(center, floor.width!!, floor.height!!)
                        .bearing(floor.bearing!!)
                        .transparency(0.5f)

                var groundOverlay =  mMap.addGroundOverlay(fpOverlay)
                return groundOverlay

            }

            return null

        }

        fun groundOverlayFactory(mMap: GoogleMap, center :LatLng, width: Float, height: Float, bearing: Float , bitmapDescriptor: BitmapDescriptor): GroundOverlay?{

            if (mMap != null) {
                val fpOverlay = GroundOverlayOptions()
                        .image(bitmapDescriptor)
                        .visible(false)
                        .position(center,width, height)
                        .bearing(bearing)
                        .transparency(0.5f)

                var groundOverlay =  mMap.addGroundOverlay(fpOverlay)
                return groundOverlay

            }

            return null

        }
    }
}