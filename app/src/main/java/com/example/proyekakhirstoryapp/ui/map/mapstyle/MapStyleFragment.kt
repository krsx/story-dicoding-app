package com.example.proyekakhirstoryapp.ui.map.mapstyle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.databinding.FragmentBottomSheetMapStyleBinding
import com.example.proyekakhirstoryapp.ui.map.MapViewModel
import com.example.proyekakhirstoryapp.ui.viewmodelfactory.ViewModelFactory
import com.example.proyekakhirstoryapp.utils.MapStyle
import com.example.proyekakhirstoryapp.utils.MapType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MapStyleFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetMapStyleBinding? = null

    private lateinit var factory: ViewModelFactory
    private val mapViewModel: MapViewModel by activityViewModels { factory }

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetMapStyleBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = ViewModelFactory.getInstance(requireContext())

        observer()
        dialogClickListener()
        hideMapStyle()
    }

    private fun observer() {
        mapViewModel.getMapType().observe(this) { type ->
            when (type) {
                MapType.NORMAL -> {
                    highlightSelectedMapType(MapType.NORMAL)
                    hideMapStyle(false)
                }
                MapType.SATELLITE -> highlightSelectedMapType(MapType.SATELLITE)
                MapType.TERRAIN -> highlightSelectedMapType(MapType.TERRAIN)
                else -> highlightSelectedMapType(MapType.NORMAL)
            }
        }

        mapViewModel.getMapStyle().observe(this) { style ->
            when (style) {
                MapStyle.NORMAL -> highlightSelectedMapStyle(MapStyle.NORMAL)
                MapStyle.NIGHT -> highlightSelectedMapStyle(MapStyle.NIGHT)
                MapStyle.SILVER -> highlightSelectedMapStyle(MapStyle.SILVER)
                else -> highlightSelectedMapStyle(MapStyle.NORMAL)
            }
        }
    }

    private fun dialogClickListener() {
        binding.cvMapDefault.setOnClickListener {
            mapViewModel.saveMapType(MapType.NORMAL)
            displayToast(getString(R.string.map_type_normal))
            dismiss()
        }

        binding.cvMapSatellite.setOnClickListener {
            mapViewModel.saveMapType(MapType.SATELLITE)
            displayToast(getString(R.string.map_type_satellite))
            dismiss()
        }

        binding.cvMapTerrain.setOnClickListener {
            mapViewModel.saveMapType(MapType.TERRAIN)
            displayToast(getString(R.string.map_type_terrain))
            dismiss()
        }

        binding.cvMapStyleDefault.setOnClickListener {
            mapViewModel.saveMapStyle(MapStyle.NORMAL)
            displayToast(getString(R.string.map_style_normal))
            dismiss()
        }

        binding.cvMapStyleNight.setOnClickListener {
            mapViewModel.saveMapStyle(MapStyle.NIGHT)
            displayToast(getString(R.string.map_style_night))
            dismiss()
        }

        binding.cvMapStyleSilver.setOnClickListener {
            mapViewModel.saveMapStyle(MapStyle.SILVER)
            displayToast(getString(R.string.map_style_silver))
            dismiss()
        }
    }

    private fun changeColor(color: String): Int {
        return when (color) {
            ACCENT_COLOR -> ContextCompat.getColor(requireContext(), R.color.teal_200)
            else -> ContextCompat.getColor(requireContext(), R.color.black)
        }
    }

    private fun highlightSelectedMapType(type: MapType) {
        when (type) {
            MapType.NORMAL -> {
                binding.ivMapDefault.setPadding(5, 5, 5, 5)
                binding.tvMapDefault.setTextColor(changeColor(ACCENT_COLOR))
                binding.tvMapSatellite.setTextColor(changeColor(MAIN_COLOR))
                binding.tvMapTerrain.setTextColor(changeColor(MAIN_COLOR))
            }
            MapType.SATELLITE -> {
                binding.tvMapDefault.setTextColor(changeColor(MAIN_COLOR))
                binding.ivMapSatellite.setPadding(5, 5, 5, 5)
                binding.tvMapSatellite.setTextColor(changeColor(ACCENT_COLOR))
                binding.tvMapTerrain.setTextColor(changeColor(MAIN_COLOR))
            }
            MapType.TERRAIN -> {
                binding.tvMapDefault.setTextColor(changeColor(MAIN_COLOR))
                binding.tvMapSatellite.setTextColor(changeColor(MAIN_COLOR))
                binding.ivMapTerrain.setPadding(5, 5, 5, 5)
                binding.tvMapTerrain.setTextColor(changeColor(ACCENT_COLOR))
            }
        }
    }

    private fun highlightSelectedMapStyle(style: MapStyle) {
        when (style) {
            MapStyle.NORMAL -> {
                binding.ivMapStyleDefault.setPadding(5, 5, 5, 5)
                binding.tvMapStyleNormal.setTextColor(changeColor(ACCENT_COLOR))
                binding.tvMapStyleNight.setTextColor(changeColor(MAIN_COLOR))
                binding.tvMapStyleSilver.setTextColor(changeColor(MAIN_COLOR))
            }
            MapStyle.NIGHT -> {
                binding.tvMapStyleNormal.setTextColor(changeColor(MAIN_COLOR))
                binding.ivMapStyleNight.setPadding(5, 5, 5, 5)
                binding.tvMapStyleNight.setTextColor(changeColor(ACCENT_COLOR))
                binding.tvMapStyleSilver.setTextColor(changeColor(MAIN_COLOR))
            }
            MapStyle.SILVER -> {
                binding.tvMapStyleNormal.setTextColor(changeColor(MAIN_COLOR))
                binding.tvMapStyleNight.setTextColor(changeColor(MAIN_COLOR))
                binding.ivMapStyleSilver.setPadding(5, 5, 5, 5)
                binding.tvMapStyleSilver.setTextColor(changeColor(ACCENT_COLOR))
            }
        }
    }

    private fun hideMapStyle(isHide: Boolean = true) {
        if (isHide) {
            Log.e("MapStyleFragment", "GONE")
            binding.tvMapStyle.visibility = View.GONE
            binding.cvMapStyleNight.visibility = View.GONE
            binding.cvMapStyleDefault.visibility = View.GONE
            binding.cvMapStyleSilver.visibility = View.GONE
            binding.tvMapStyleNight.visibility = View.GONE
            binding.tvMapStyleNormal.visibility = View.GONE
            binding.tvMapStyleSilver.visibility = View.GONE
        } else {
            Log.e("MapStyleFragment", "VISIBLE")
            binding.tvMapStyle.visibility = View.VISIBLE
            binding.cvMapStyleNight.visibility = View.VISIBLE
            binding.cvMapStyleDefault.visibility = View.VISIBLE
            binding.cvMapStyleSilver.visibility = View.VISIBLE
            binding.tvMapStyleNight.visibility = View.VISIBLE
            binding.tvMapStyleNormal.visibility = View.VISIBLE
            binding.tvMapStyleSilver.visibility = View.VISIBLE
        }
    }

    private fun displayToast(msg: String) {
        return Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val ACCENT_COLOR = "TEAL"
        const val MAIN_COLOR = "BLACK"
    }

}