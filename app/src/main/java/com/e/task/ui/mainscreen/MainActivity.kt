package com.e.task.ui.mainscreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.e.task.BR
import com.e.task.R
import com.e.task.base.BaseActivity
import com.e.task.databinding.ActivityMainBinding
import com.e.task.utils.NotificationHelper
import com.e.task.utils.RequestPermission
import com.e.task.utils.ViewModelProviderFactory
import com.ex.slt.geofance.GeofenceRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator , OnMapReadyCallback,GoogleMap.OnMapLongClickListener {
    
    lateinit var geofenceClient:GeofencingClient

    lateinit var currentLocation:Location

    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    var mCircle: Circle? = null

    var radiusInMeters  = 10.0F

    private val factory: ViewModelProviderFactory by inject()

    override val viewModel: MainViewModel
        get() = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

    private var activityMainBinding: ActivityMainBinding? = null

    override val bindingVariable: Int
        get() = BR.viewModel

    override val isFullScreen: Boolean
        get() = false

    override val isStatusBar: Boolean
        get() = true

    override val layoutId: Int
        get() = R.layout.activity_main

    lateinit var gMap :GoogleMap

    private var mMyLocMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = getViewDataBinding()
        viewModel.setNavigator(this)

        var geofenceRepository= GeofenceRepository(this)

        geofenceRepository.getCurrentLocation1()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        geofenceClient=LocationServices.getGeofencingClient(this)

        viewModel.setBroadcastReceiver(this)
    }

    @SuppressLint("MissingPermission")
    fun mapInitialize() {
        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this@MainActivity)

            }
        }
    }


    override fun onStart() {
        super.onStart()
        openRequestPermission()
    }

    private fun openRequestPermission() {
        if (requestPermission!!.checkPermission(RequestPermission.PERMISSION_CALL_PHONE)&&requestPermission!!.checkPermission(RequestPermission.PERMISSION_READ_STORAGE)&&requestPermission!!.checkPermission(
                RequestPermission.PERMISSION_WRITE_STORAGE
            )){
            mapInitialize()



        } else {
            requestPermission!!.permissionRequestShow(
                object : RequestPermission.PermissionCallBack {
                    override fun callBack(
                        grantAllPermission: Boolean,
                        deniedAllPermission: Boolean,
                        permissionResultList: List<Boolean>
                    ) {
                        if (grantAllPermission) {

                            mapInitialize()

                        }
                    }
                },

                RequestPermission.PERMISSION_CALL_PHONE, RequestPermission.PERMISSION_READ_STORAGE, RequestPermission.PERMISSION_WRITE_STORAGE
            )
        }
    }
    @SuppressLint("MissingPermission")
    override fun getData() {
        Log.e("Tag ","Location Chnage"+currentLocation.longitude)
        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                var notificationHelper= NotificationHelper(this)
                notificationHelper.sendHighPriorityNotification("Out Of Range","Out Of Range",MainActivity::class.java)
                drawCircle(LatLng(currentLocation.latitude,currentLocation.longitude))
            }
        }

    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {

        Log.e("Map----","Readyyyy")
        gMap=googleMap
        googleMap.isMyLocationEnabled=true
        gMap.setOnMapLongClickListener(this)
        var latLng=LatLng(currentLocation.latitude,currentLocation.longitude)
        drawCircle(latLng)

    }

    @SuppressLint("MissingPermission")
    private fun drawCircle(point: LatLng) {

        gMap.clear()
        if (gMap != null) {
            val options = MarkerOptions()
                .position(point)
            mMyLocMarker = gMap.addMarker(options)
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17f))

        }

        mCircle = gMap.addCircle(
            CircleOptions()
                .center(LatLng(point.latitude, point.longitude))
                .radius(radiusInMeters.toDouble())
                .fillColor(0x44ff0000)
                .strokeColor(-0x10000)
                .strokeWidth(1f)
        )
        var geofenceRepository= GeofenceRepository(this)
        geofenceRepository.add(point, radiusInMeters)

    }

    override fun onMapLongClick(latLng: LatLng) {
        drawCircle(latLng)
    }

}
