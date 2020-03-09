package fr.thomas.lefebvre.toutougo.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentMapBinding
import fr.thomas.lefebvre.toutougo.ui.userDashboard.DashBoardViewModel
import fr.thomas.lefebvre.toutougo.ui.place.MainViewModel
import android.location.Geocoder
import android.location.Address
import com.google.android.material.snackbar.Snackbar
import fr.thomas.lefebvre.toutougo.ui.placeDetails.DetailPlaceFragment
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
    GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {


    // -----                          VARIABLES                            --------
    private lateinit var locationViewModel: DashBoardViewModel

    private lateinit var placeViewModel: MainViewModel

    private lateinit var binding: FragmentMapBinding

    //google map
    private lateinit var mMapView: MapView
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private val firstMap: Boolean = true

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
            ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        //init view with data binding
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        mMapView = binding.mapView
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()

        mMapView.getMapAsync(this)
        //init fused location client service
        mFusedLocationProviderClient =//init location provider
            LocationServices.getFusedLocationProviderClient(requireContext())
        binding.viewModel = placeViewModel




        when (placeViewModel.isPlaceOrEvent.value) {
            getString(R.string.event) -> placeViewModel.messageMapFragment.value =
                "Clic sur le lieu ou tu veux créer ton événement"
            getString(R.string.place) -> placeViewModel.messageMapFragment.value =
                "Clic sur la carte pour indiquer l'emplacement du lieu à créer."
            null -> placeViewModel.messageMapFragment.value =
                "Visualise les lieux et évenements en cliquant sur les icones"
        }

        binding.lifecycleOwner = activity


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init the service map

        //get place for create marker
        getPlaceForCreateMarker()

        //init infos button
        onClickInfosButton()

        Log.d("MAP", "On view created + context: ${placeViewModel.isPlaceOrEvent.value.toString()}")
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()

        Log.d("MAP", "On pause")
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
        placeViewModel.isPlaceOrEvent.value = null
        Log.d("MAP", "On destroy")
    }

    override fun onResume() {
        mMapView.onResume()
        super.onResume()


        Log.d("MAP", "On resume")
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
        Log.d("MAP", "On start")
    }

    override fun onStop() {
        Log.d("MAP", "On stop")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mMapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }


    // -----                          MAP                            --------


    override fun onMapReady(googleMap: GoogleMap?) {

        Log.d("MAP", "onMapReady")

        MapsInitializer.initialize(context)
        mGoogleMap = googleMap!!
        setUpMap()
        mGoogleMap.setOnMapClickListener(this)
        mGoogleMap.setOnInfoWindowClickListener(this)
        mGoogleMap.setOnMarkerClickListener(this)


        placeViewModel.listPlace.observe(this, Observer { listPlace ->
            var index: Int = 0
            for (place in listPlace) {
                val latLng = LatLng(place.lat, place.lng)
                createMarkerRoad(latLng, place.name, place.uid, index)
                index += 1
            }
        })


    }


    private fun setUpMap() {
        if (locationViewModel.lastLatitute.value != null) {
            mGoogleMap.uiSettings.isMyLocationButtonEnabled = true//enable button location
            mGoogleMap.isMyLocationEnabled = true//enable location service
            Log.d("MAP", locationViewModel.lastLatitute.value.toString())
            val currentLatLng = LatLng(
                locationViewModel.lastLatitute.value!!,
                locationViewModel.lastLongitude.value!!
            )
            if (firstMap) {

            }
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
        }
        Log.d("MAP", locationViewModel.lastLatitute.value.toString())

    }

    private fun getPlaceForCreateMarker() {
        placeViewModel.getPlace(
            locationViewModel.lastLatitute.value!!,
            locationViewModel.lastLongitude.value!!
        )
    }

    private fun pickAddressForPlace() {
        if (markerClick != null) {//pick address only on marker click
            if (placeViewModel.adressPlace.value != null) {//if address is correct
                view!!.findNavController().navigate(R.id.action_mapFragment_to_createPlaceFragment)
                Log.d("MAP", "click on infos windows")
            } else {//address not correct
                Snackbar.make(
                    requireView(),
                    getString(R.string.complete_address),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        } else {// no pick address on marker place or event
            Snackbar.make(
                requireView(),
                getString(R.string.no_choice_lieu),
                Snackbar.LENGTH_LONG
            )
                .show()
        }
    }

    private fun pickPlaceForEvent(marker: Marker?) {

        if (marker!!.snippet == "Chemin") {
            placeViewModel.placeEvent.value =
                placeViewModel.listPlace.value?.get(marker.zIndex.toInt())

            placeViewModel.namePlaceEvent.value =
                placeViewModel.listPlace.value?.get(marker.zIndex.toInt())!!.name

            view!!.findNavController().navigate(R.id.action_mapFragment_to_createEventFragment)

        }
    }


    override fun onInfoWindowClick(marker: Marker?) {

        when (placeViewModel.isPlaceOrEvent.value) {
            getString(R.string.event) -> pickPlaceForEvent(marker)
            getString(R.string.place) -> pickAddressForPlace()
            null -> if (marker!!.snippet == "Chemin") {
                placeViewModel.detailPlace.value =
                    placeViewModel.listPlace.value?.get(marker.zIndex.toInt())
                fragmentDetailPlace()
            }
        }
    }


    override fun onMarkerClick(marker: Marker?): Boolean {

        if (markerClick != null) {//remove marker click if click on other marker (place or event)
            markerClick!!.remove()
            markerClick = null
        }

        return false//show info windows
    }


    override fun onMapClick(latLng: LatLng?) {
        if (placeViewModel.isPlaceOrEvent.value == getString(R.string.place)) {//create marker only if the context is create place

            if (markerClick != null) {//delete marker if already draw
                markerClick!!.remove()
            }

            createMarker(latLng, getString(R.string.emplacement))//create a new marker


            val geocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses: List<Address>

            addresses = geocoder.getFromLocation(//get the address of the marker click on map
                latLng!!.latitude,
                latLng!!.longitude,
                1
            ) // Here 1 represent max location result to returned, by documents
            if (addresses.isNotEmpty()) {
                val address =
                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                Log.d("MAP", address.toString())
                placeViewModel.adressPlace.value = address.toString()
                placeViewModel.latPlace.value = latLng!!.latitude
                placeViewModel.lngPlace.value = latLng!!.longitude
            } else {
                placeViewModel.adressPlace.value = null
            }
        }


    }

    // ----------------------- MARKERS --------------------------------------

    private fun createMarker(latLng: LatLng?, titleMarker: String) {
        markerClick = mGoogleMap.addMarker(
            MarkerOptions().position(latLng!!).title(
                titleMarker
            )


        )
        markerClick!!.showInfoWindow()
    }


    private fun createMarkerRoad(
        latLng: LatLng?,
        titleMarker: String,
        idPlace: String,
        index: Int
    ) {
        mGoogleMap.addMarker(
            MarkerOptions()
                .position(latLng!!)
                .title(titleMarker)
                .snippet("Chemin")
                .zIndex(index.toFloat())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parc))

        )
    }


    // ------------ ON CLICK BUTTON ------

    private fun onClickInfosButton() {
        binding.buttonInfos.setOnClickListener {
            Snackbar.make(
                requireView(),
                placeViewModel.messageMapFragment.value!!,
                Snackbar.LENGTH_LONG
            )
                .show()
        }
    }


    private fun fragmentDetailPlace() {

        val detailFragment = DetailPlaceFragment()
        val fragmentManager = getFragmentManager()
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
            .add(R.id.nav_host_fragment, detailFragment)
            .addToBackStack("map")
        fragmentTransaction.commit()


    }


}