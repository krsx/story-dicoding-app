package com.example.proyekakhirstoryapp.ui.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat

import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.databinding.ActivityMapsBinding
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val boundsBuilder = LatLngBounds.Builder()
    private var _binding: ActivityMapsBinding? = null

    private lateinit var factory: ViewModelFactory
    private val mapViewModel: MapViewModel by viewModels { factory }

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getUserStories()

        mapViewModel.message.observe(this) {
            displayToast(it)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Log.e("MapViewModel", "$mMap")
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()

        mapViewModel.userStories.observe(this) { stories ->
            stories.let {
                if (it != null) {
                    for (story in it) {
                        val lat: Double = story?.lat as Double
                        val lon: Double = story.lon as Double

                        val latLng = LatLng(lat, lon)
                        googleMap.addMarker(
                            MarkerOptions().position(latLng).title(story.name)
                        )?.tag = story.id
                        boundsBuilder.include(latLng)

                    }
                    val bounds: LatLngBounds = boundsBuilder.build()
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            100
                        )
                    )
                }
            }
        }
    }

    private fun getUserStories() {
        mapViewModel.getUserToken().observe(this) { token ->
            mapViewModel.getUserStoryMap(token)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun displayToast(msg: String) {
        return Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}