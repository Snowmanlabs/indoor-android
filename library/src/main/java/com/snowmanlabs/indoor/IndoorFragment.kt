package com.snowmanlabs.indoor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snowmanlabs.indoor.domain.Position
import com.snowmanlabs.indoor.model.Connector
import com.snowmanlabs.indoor.model.Floor
import com.snowmanlabs.indoor.model.POI
import com.snowmanlabs.indoor.utils.LatLngInterpolator
import com.snowmanlabs.indoor.utils.MapsUtils
import com.snowmanlabs.indoor.utils.MarkerAnimation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import java.util.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.MapsInitializer
import com.snowmanlabs.library.R
import kotlinx.android.synthetic.main.fragment_indoor.*

/**
 * Created by diefferson on 06/10/17.
 */
abstract class IndoorFragment : Fragment(), IIndoorView {

    private val PERMISSIONS_REQUEST_LOCATION = 2

    private lateinit var mMap: GoogleMap
    private var myPosition : Marker? =  null

    private val mGroudOverlays = HashMap<Int, GroundOverlay>()
    private val mPoisMarkes = HashMap<Int, Marker>()
    private val mConnectorsMarkes = HashMap<Int, Marker>()
    private var indoorPresenter: IndoorPresenter? =  null

    private var mFloors: List<Floor>?= null
    private var mPois: List<POI>?= null
    private var mConnectors: List<Connector>?= null

    abstract fun getFloors():List<Floor>?
    abstract fun getPois(): List<POI>?
    abstract fun getConnectors(): List<Connector>?


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_indoor, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        try {
            MapsInitializer.initialize(activity.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView.getMapAsync({ map ->
            mMap = map
            initializeFloors()
            initializePois()
            initializeConnectors()
            startTrackingService(true, false)
        })
    }

    private fun getPresenter(): IndoorPresenter {
        if(indoorPresenter == null){
            indoorPresenter = IndoorPresenter(context, this@IndoorFragment)
        }

        return indoorPresenter!!
    }

    override fun setMyPosition(position : Position){

        var myLatLang =  LatLng(position.latitude, position.longitude)

        if(myPosition == null){
            myPosition = MapsUtils.markerFactory(mMap, position.latitude, position.longitude, "eu", "eu")
        }else{
            MarkerAnimation.animateMarkerToGB(myPosition!!,
                    myLatLang, LatLngInterpolator.Linear())
        }

        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLatLang, 22f)
        mMap.animateCamera(cameraUpdate)
    }

    override fun selectFloor(floor: Int) {

        mGroudOverlays.forEach {
            it.value.isVisible = it.key == floor
        }
    }


    private fun startTrackingService(checkPermission: Boolean, permission: Boolean) {
        var permission = permission
        if (checkPermission) {
            val missingPermissions = HashSet<String>()
            if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                missingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                missingPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (missingPermissions.isEmpty()) {
                permission = true
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(missingPermissions.toTypedArray(),
                            PERMISSIONS_REQUEST_LOCATION)
                }
                return
            }
        }

        getPresenter().start()
    }

    private fun initializeConnectors(){

        if(mConnectors == null){
            mConnectors = getConnectors()
        }

        if(mConnectors == null) throw IllegalStateException("mConnectors  = null")

        mConnectors!!.forEach {
            val poi =  MapsUtils.markerFactory(mMap, it.latitude, it.longitude, "", "")
            mConnectorsMarkes[it.id] = poi
        }
    }


    private fun initializePois(){

        if(mPois == null){
            mPois = getPois()
        }

        if(mPois == null) throw IllegalStateException("mPois  = null")

        mPois!!.forEach {
            val poi =  MapsUtils.markerFactory(mMap,it.latitude, it.longitude, it.name!!, it.description!!)
            mPoisMarkes[it.id!!] = poi
        }
    }

    private fun initializeFloors(){

        if(mFloors == null){
            mFloors = getFloors()
        }

        if(mFloors == null || mFloors!!.isEmpty()) throw IllegalStateException("mFloors  = null or size is 0")

        mFloors!!.forEach {
            var groundOverlay =  MapsUtils.groundOverlayFactory(mMap, it)
            mGroudOverlays[it.number!!] = groundOverlay!!
        }

        if(mFloors!![0] != null){
            selectFloor(0)
            var cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(mFloors!![0].latitude!!, mFloors!![0].longitude!!), 20f)
            mMap.animateCamera(cameraUpdate)
        }

    }

    private fun hasPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return true
        }

        if(activity!= null) return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

        return true
    }


    override fun onDestroy() {
        super.onDestroy()

        if(mapView != null){  mapView.onDestroy() }

        if (indoorPresenter != null) {
            getPresenter().stop()
            indoorPresenter = null
        }
    }

    override fun onResume() {
        super.onResume()
        if(mapView != null){ mapView.onResume() }
    }

    override fun onPause() {
        super.onPause()
        if(mapView != null){  mapView.onPause() }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if(mapView != null){  mapView.onLowMemory() }
    }

    companion object {
        val TAG = IndoorFragment::class.java.simpleName
    }
}
