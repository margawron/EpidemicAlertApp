package com.github.margawron.epidemicalertapp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.margawron.epidemicalertapp.util.PermissionSafeguard
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class DeleteMe_MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private val TAG = "Epidemic Alert"

    private val permissionSafeguard = PermissionSafeguard(
        this,
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.INTERNET
        ),
        50
    )

    private val locationRequest = LocationRequest().apply {
        this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        this.interval = 1000
        this.fastestInterval = 500
    }

    private var marker: Marker? = null
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {

                Log.d(TAG, " measurement error " + location.accuracy)
                val position = LatLng(location.latitude, location.longitude)
                Log.d(TAG, " position " + position)

                marker?.remove()
                marker = googleMap?.addMarker(MarkerOptions().apply {
                    position(position)
                    title(getString(R.string.HERE_YOU_ARE))
                })
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 18F))
                googleMap?.animateCamera(CameraUpdateFactory.zoomIn())
                googleMap?.animateCamera(CameraUpdateFactory.zoomTo(18F), 2000, null)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startLocationUpdates()

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when {
            requestCode == 50 -> {
                startLocationUpdates()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun startLocationUpdates() {
        permissionSafeguard.doIfPossibleOrRequestPermission {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onMapReady(incomingGoogleMap: GoogleMap?) {
        googleMap = incomingGoogleMap
    }


}