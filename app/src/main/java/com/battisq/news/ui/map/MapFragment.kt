package com.battisq.news.ui.map

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.battisq.news.R
import com.battisq.news.databinding.MapFragmentBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.koin.android.ext.android.get


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    companion object {
        fun newInstance() = MapFragment()
    }

    private var viewModel: MapViewModel = get()
    private var binding: MapFragmentBinding? = null
    private val mBinding: MapFragmentBinding get() = binding!!

    private val LOCATION_REQ_CODE: Int = 11
    private var permissionDenied = false
    private lateinit var mGoogleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MapFragmentBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportMapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment?.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
    }

    override fun onMyLocationButtonClick(): Boolean {
        if (!viewModel.isGeolocationEnabled())
            Toast.makeText(context, getString(R.string.text_turn_on_geolocation), Toast.LENGTH_LONG)
                .show()

        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap == null)
            return

        mGoogleMap = googleMap
        mGoogleMap.setOnMyLocationButtonClickListener(this)
        enabledMyLocation()
    }

    private fun enabledMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap.isMyLocationEnabled = true
        } else {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_REQ_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_REQ_CODE)
            return

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enabledMyLocation()
        } else {
            permissionDenied = true
        }
    }

    override fun onResume() {
        super.onResume()

        if (permissionDenied) {
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    private fun showMissingPermissionError() {
        Toast.makeText(context, getString(R.string.text_permission_denied), Toast.LENGTH_SHORT)
            .show()
    }
}