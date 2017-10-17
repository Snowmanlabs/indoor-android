package com.snowmanlabs.indoor

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import com.snowmanlabs.indoor.domain.Position
import com.snowmanlabs.indoor.domain.Dijkstra
import com.snowmanlabs.indoor.domain.Floor
import com.snowmanlabs.indoor.domain.Graph
import com.snowmanlabs.indoor.domain.POI
import com.snowmanlabs.indoor.utils.*
import com.snowmanlabs.library.R
import kotlinx.android.synthetic.main.fragment_indoor.*
import java.util.LinkedList
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.squareup.picasso.Picasso
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.os.AsyncTask
import android.util.Log
import android.view.*
import android.widget.Toast
import java.io.IOException

/**
 * Created by diefferson on 06/10/17.
 */
@SuppressWarnings("MissingPermission")
abstract class IndoorFragment : Fragment(), IIndoorView {

    private val PERMISSIONS_REQUEST_LOCATION = 1

    //MAPS
    private lateinit var mMap: GoogleMap
    private var myPosition : Marker? =  null
    private var atualRoute : Polyline? =  null
    private val mGroudOverlays = HashMap<Int, GroundOverlay>()
    private val mPoisMarkes = HashMap<Int, Marker>()

    //MODELS
    private var mFloors: List<Floor>?= null
    private var mPois: List<POI>?= null
    private var poisMap = HashMap<Int, POI>()
    private var graph: Graph?= null

    abstract fun getFloors():List<Floor>
    abstract fun getPois(): List<POI>

    private var zoomMyPosition = true
    private var showMyPosition = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_indoor, container, false)
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
            mMap.mapType = GoogleMap.MAP_TYPE_NONE
            mMap.setLatLngBoundsForCameraTarget(LatLngBounds(LatLng(-25.446111119099353 , -49.35726698487997), LatLng(-25.442681507819206 , -49.35494083911181)))

            mMap.setOnMapClickListener(GoogleMap.OnMapClickListener() {
                fun onMapClick (point:LatLng ){
                    Toast.makeText(getContext(),
                            point.latitude.toString() + ", " + point.longitude,
                            Toast.LENGTH_SHORT).show()
                }
            })

            mMap.setMinZoomPreference(18F)
            mMap.uiSettings.isMyLocationButtonEnabled = false

            initializeFloors()
            initializePois()
            mMap.uiSettings.isMapToolbarEnabled = false

            mMap.setOnMapClickListener {
                Log.i(TAG, ""+it.latitude+" - "+it.longitude )
            }

            startTrackingService(true)

        })

        mylocation_fab.setOnClickListener { displayMyPosition() }
        route_fab.setOnClickListener { showDirectionDialog() }
    }

    private fun showRoute(poiOrigin: POI, poiDestiny: POI){

        var path :LinkedList<POI>? = null
        val coords = ArrayList<LatLng>()

        initializeGraph()
        graph = Dijkstra.calculateShortestPathFromSource(graph!!, poiOrigin)

        graph!!.nodes.forEach {
            if(it.id == poiDestiny.id){
                path = it.shortestPath
            }
        }

        path!!.forEach {
            coords.add(LatLng(it.latitude!!, it.longitude!!))
        }

        coords.add(LatLng(poiDestiny.latitude!!, poiDestiny.longitude!!))

        if(atualRoute == null){
            atualRoute = mMap.addPolyline( PolylineOptions().addAll(coords))
        }else{
            atualRoute!!.points = coords
        }
    }

    private fun showDirectionDialog() {

        val directionDialog = Dialog(context)
        directionDialog.setContentView(R.layout.directions_dialog)

         var list = ArrayList<POI>()

        mPois!!.forEach {
            if(it.type!!.equals(POI.POI_TYPE)){
                list.add(it)
            }
        }

        val adapter =  ArrayAdapter<POI>(context, android.R.layout.simple_spinner_item, list)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val originSpinner = directionDialog.findViewById<Spinner>(R.id.origin)
        val destinySpinner = directionDialog.findViewById<Spinner>(R.id.destiny)
        val confirmButtom = directionDialog.findViewById<Button>(R.id.confirm_btn)
        val cancelButtom = directionDialog.findViewById<Button>(R.id.cancel_btn)

        originSpinner.adapter = adapter
        destinySpinner.adapter = adapter

        cancelButtom.setOnClickListener({ v: View -> directionDialog.dismiss() })

        confirmButtom.setOnClickListener({ v: View ->
            showRoute(originSpinner.selectedItem as POI, destinySpinner.selectedItem as POI)
            directionDialog.dismiss()
        })

        val  lp = WindowManager.LayoutParams()
        lp.copyFrom(directionDialog.window.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        directionDialog.window.attributes = lp

        directionDialog.show()

    }

    private fun displayMyPosition(){

        if(showMyPosition){
            mMap.isMyLocationEnabled = true
            showMyPosition = !showMyPosition
        }else{
            mMap.isMyLocationEnabled = false
            showMyPosition = !showMyPosition
        }
    }

    override fun setMyPosition(position : Position){
        loading.visibility = View.GONE


        val myLatLang =  LatLng(position.latitude, position.longitude)

        if(myPosition == null){
            myPosition = MapsUtils.markerFactory(mMap, position.latitude, position.longitude, "eu", "eu", R.drawable.blue_dot)
        }else{
            MarkerAnimation.animateMarkerToGB(myPosition!!,
                    myLatLang, LatLngInterpolator.Linear())
        }

        zoomMyPosition = false
    }

    override fun selectFloor(floor: Int) {
        mGroudOverlays.forEach {
            it.value.isVisible = it.key == floor
        }
    }

    private fun startTrackingService(checkPermission: Boolean) {
        if (checkPermission) {
            val missingPermissions = HashSet<String>()
            if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                missingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (!hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                missingPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (!missingPermissions.isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(missingPermissions.toTypedArray(),
                            PERMISSIONS_REQUEST_LOCATION)
                }
                return
            }
        }
    }

    private fun cleanPaths(){
        mPois!!.forEach {
            it.shortestPath =  LinkedList<POI>()
            it.distance  = Float.MAX_VALUE
        }
    }

    private  fun initializeGraph(){
        cleanPaths()
        graph = Graph()

        mPois!!.forEach {
            graph!!.addNode(it)
        }
    }

    private fun initializePois(){

        if(mPois == null){
            mPois = getPois()
        }

        if(mPois!!.isEmpty()) throw IllegalStateException("mPois size is 0")

        mPois!!.forEach {
            val poi =  MapsUtils.markerFactory(mMap,it.latitude, it.longitude, it.name!!, it.description!!, R.drawable.ic_poi)
            poi.isVisible = false
            mPoisMarkes[it.id!!] = poi
            poisMap.put(it.id!!, it)
        }
    }

    private fun initializeFloors(){

        if(mFloors == null){
            mFloors = getFloors()
        }

        if(mFloors!!.isEmpty()) throw IllegalStateException("mFloors size is 0")

        mFloors!!.forEach {
            AddGroundOverlay().execute(it.number.toString(), it.image, it.latitude.toString(), it.longitude.toString(),it.bearing.toString(), it.width.toString(), it.height.toString())
        }

    }

    private inner class AddGroundOverlay : AsyncTask<String, Int, BitmapDescriptor>() {

        internal var bitmapDescriptor: BitmapDescriptor? = null
        var number:Int? = null
        var myUrl:String? = null
        var latitude:Double? = null
        var longitude:Double? = null
        var bearing:Float? = null
        var width:Float? = null
        var height:Float? = null

        override fun doInBackground(vararg params: String): BitmapDescriptor? {
            number = params[0].toInt()
            myUrl = params[1]
            latitude  = params[2].toDouble()
            longitude = params[3].toDouble()
            bearing = params[4].toFloat()
            width = params[5].toFloat()
            height = params[6].toFloat()

            try {
                bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(Picasso.with(activity).load(myUrl).get())
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return bitmapDescriptor
        }

        override fun onPostExecute(icon: BitmapDescriptor) {

            try {

                val groundOverlay =  MapsUtils.groundOverlayFactory(mMap, LatLng(latitude!!, longitude!!),width!!, height!!, bearing!!, icon)
                mGroudOverlays[number!!] = groundOverlay!!

                if(mFloors!![0] != null){
                    selectFloor(0)
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(mFloors!![0].latitude!!, mFloors!![0].longitude!!), 19f)
                    mMap.animateCamera(cameraUpdate)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

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
