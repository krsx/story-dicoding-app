package com.example.proyekakhirstoryapp.ui.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat

import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.databinding.ActivityMapsBinding
import com.example.proyekakhirstoryapp.utils.MapStyle
import com.example.proyekakhirstoryapp.ui.map.mapstyle.MapStyleFragment
import com.example.proyekakhirstoryapp.utils.MapType
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_map_style -> {
                val mapStyleFragment = MapStyleFragment()
                mapStyleFragment.show(supportFragmentManager, TAG)
            }
        }
        return super.onOptionsItemSelected(item)
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
                            500
                        )
                    )
                }
            }
        }

        mapViewModel.getMapStyle().observe(this) { style ->
            when (style) {
                MapStyle.NORMAL -> setMapStyle(googleMap, MapStyle.NORMAL)
                MapStyle.NIGHT -> setMapStyle(googleMap, MapStyle.NIGHT)
                MapStyle.SILVER -> setMapStyle(googleMap, MapStyle.SILVER)
                else -> setMapStyle(googleMap, MapStyle.NORMAL)
            }
        }

        mapViewModel.getMapType().observe(this) { type ->
            when (type) {
                MapType.NORMAL -> setMapType(googleMap, MapType.NORMAL)
                MapType.SATELLITE -> setMapType(googleMap, MapType.SATELLITE)
                MapType.TERRAIN -> setMapType(googleMap, MapType.TERRAIN)
                else -> setMapType(googleMap, MapType.NORMAL)
            }
        }
    }

    private fun setMapType(mMap: GoogleMap, type: MapType) {
        when (type) {
            MapType.NORMAL -> mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            MapType.SATELLITE -> mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            MapType.TERRAIN -> mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
    }

    private fun setMapStyle(mMap: GoogleMap, style: MapStyle) {
        when (style) {
            MapStyle.NORMAL -> mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_syke_normal
                )
            )
            MapStyle.NIGHT -> mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style_night
                )
            )
            MapStyle.SILVER -> mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style_silver
                )
            )
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

    companion object {
        const val TAG = "MapActivity"
    }
}