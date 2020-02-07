package fr.thomas.lefebvre.toutougo.ui.map

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentMapBinding
import fr.thomas.lefebvre.toutougo.ui.dashboard.DashBoardViewModel
import fr.thomas.lefebvre.toutougo.ui.place.PlaceViewModel

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
     GoogleMap.OnMapClickListener {

    // -----                          VARIABLES                            --------
    private lateinit var locationViewModel: DashBoardViewModel

    private lateinit var placeViewModel:PlaceViewModel

    //google map
    private lateinit var mMapView: MapView
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    //marker on long click
    private var markerClick: Marker? = null



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    }



    // -----                          LIFE CYCLE                            --------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MAP", "On CreateView")
        locationViewModel =//init location view model
            ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)

        placeViewModel =//init location view model
                ViewModelProviders.of(activity!!).get(PlaceViewModel::class.java)
        //init view with data binding
        val binding: FragmentMapBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)

        //init the service map
        mMapView = binding.mapView
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)
        //init fused location client service
        mFusedLocationProviderClient =//init location provider
            LocationServices.getFusedLocationProviderClient(requireContext())



        binding.lifecycleOwner=activity

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MAP", "On view created")
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
        Log.d("MAP", "On pause")
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
        Log.d("MAP", "On destroy")
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()

        Log.d("MAP", "On resume")
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
        Log.d("MAP", "On start")
    }


    // -----                          MAP                            --------




    override fun onMapReady(googleMap: GoogleMap?) {

        MapsInitializer.initialize(context)
        mGoogleMap = googleMap!!
        setUpMap()
        mGoogleMap.setOnMapClickListener(this)
        mGoogleMap.setOnInfoWindowClickListener(this)


    }


    private fun setUpMap() {
            if (locationViewModel.lastLatitute.value != null) {
                mGoogleMap.uiSettings.isMyLocationButtonEnabled = true//enable button location
                mGoogleMap.isMyLocationEnabled = true//enable location service
                Log.d("MAP",locationViewModel.lastLatitute.value.toString())
                val currentLatLng = LatLng(locationViewModel.lastLatitute.value!!, locationViewModel.lastLongitude.value!!)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
            }
        Log.d("MAP",locationViewModel.lastLatitute.value.toString())

    }


    override fun onInfoWindowClick(p0: Marker?) {
        view!!.findNavController().navigate(R.id.action_mapFragment_to_createPlaceFragment)
        Log.d("MAP","click on infos windows")

    }


    override fun onMapClick(latLng: LatLng?) {
        if (markerClick != null) {
            markerClick!!.remove()
        }
        createMarker(latLng, getString(R.string.cardview_appui_long))
        placeViewModel.latPlace.value=latLng!!.latitude
        placeViewModel.lngPlace.value=latLng!!.longitude
        placeViewModel.adressPlace.value="Adresse choisie sur la carte"
    }


    private fun createMarker(latLng: LatLng?, titleMarker: String) {
        markerClick = mGoogleMap.addMarker(
            MarkerOptions().position(latLng!!).title(
                titleMarker
            )
        )
        markerClick!!.showInfoWindow()
    }





}