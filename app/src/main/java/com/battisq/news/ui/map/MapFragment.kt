package com.battisq.news.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.battisq.news.R
import com.battisq.news.databinding.MapFragmentBinding
import com.google.android.gms.maps.*


class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var viewModel: MapViewModel
    private var binding: MapFragmentBinding? = null
    private val mBinding: MapFragmentBinding get() = binding!!
    private lateinit var mGoogleMap: GoogleMap

    @SuppressLint("ResourceType")
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

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null)
            mGoogleMap = googleMap
    }
}