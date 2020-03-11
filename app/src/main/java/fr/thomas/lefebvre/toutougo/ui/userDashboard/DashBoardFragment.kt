package fr.thomas.lefebvre.toutougo.ui.userDashboard

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.databinding.FragmentDashboardBinding
import fr.thomas.lefebvre.toutougo.ui.presentation.WelcomeViewModel

class DashBoardFragment : Fragment() {

    private lateinit var mViewModel: DashBoardViewModel
    private lateinit var mViewModelWelcome: WelcomeViewModel

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var adapter:DogAdapter

    private lateinit var binding: FragmentDashboardBinding

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    }
    // ------------------------ LIFE CYCLE METHOD ----------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel =
            ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)

        mViewModelWelcome = ViewModelProviders.of(activity!!).get(WelcomeViewModel::class.java)

            mViewModelWelcome.getUserFirebase()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        binding.viewModel = mViewModel
        binding.userViewModel=mViewModelWelcome
        binding.lifecycleOwner = activity

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        setUpLocation()

        adapter = DogAdapter(DogListener { dog ->
            launchEditFragment(dog)
        },true)
        binding.dogList.adapter = adapter

        mViewModel.listDog.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        buttonAddDog()
        buttonEditProfil()
        buttonSettingUser()



        return binding.root
    }

    override fun onResume() {
        mViewModel.getDogUser()
        Log.d("DEBUG","on Resume dashboard")
        super.onResume()
    }




    private fun launchEditFragment(dog: Dog) {
        mViewModel.uidDog.value=dog.uid
        mViewModel.nameDog.value=dog.name
        mViewModel.heightDog.value=dog.height.toString()
        mViewModel.raceDog.value=dog.race
        mViewModel.ageDog.value=dog.age.toString()
        mViewModel.sexeDog.value=dog.sexe
        mViewModel.descriptionDog.value=dog.description
        mViewModel.photoDogUrl.value=dog.photoUrl
        mViewModel.uidUserDog.value=dog.uidUser
        mViewModel.refPhoto.value=dog.refPhoto
        Log.d("DEBUG",dog.refPhoto)

        view!!.findNavController().navigate(R.id.actionEditDog)
    }

    private fun setUpLocation() {
        mViewModel.setLocation(48.8,2.33)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),//check the permission location
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {//if don't request permission
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            //call location provider
            if (location != null) {
                mViewModel.setLocation(location.latitude,location.longitude)
                val currentLatLng = LatLng(location.latitude, location.longitude)

            }


        }

    }


    // ----               ON CLICK BUTTON                        -----


    private fun buttonAddDog() {
        binding.imageButtonAddDog.setOnClickListener {
            view!!.findNavController().navigate(R.id.action_welcomeFragment_to_dogInfosFragment2)
        }
    }

    private fun buttonEditProfil(){
        binding.imageButtonEditProfil.setOnClickListener {
            view!!.findNavController().navigate(R.id.actionEdit)}}


    private fun buttonSettingUser(){
        binding.imageButtonToolsUser.setOnClickListener {
            view!!.findNavController().navigate(R.id.action_welcomeFragment_to_toolsUserFragment)
        }
    }


    // ----               REQUEST PERMISSION RESULTS             -----


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}