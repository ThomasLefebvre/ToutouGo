package fr.thomas.lefebvre.toutougo.ui.detailPlace


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentDetailPlaceBinding
import fr.thomas.lefebvre.toutougo.ui.place.PlaceViewModel
import android.content.Intent
import android.net.Uri
import fr.thomas.lefebvre.toutougo.ui.place.PhotoPlaceViewModel


/**
 * A simple [Fragment] subclass.
 */
class DetailPlaceFragment : Fragment() {

    private lateinit var viewModel:PlaceViewModel
    private lateinit var binding:FragmentDetailPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=ViewModelProviders.of(activity!!).get(PlaceViewModel::class.java)


        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_detail_place,container,false)
        binding.viewModel=viewModel

        binding.lifecycleOwner=activity

        Log.d("DETAIL PLACE",viewModel.detailPlace.value!!.name)
        viewModel.getPhoto()

        onButtonMap()


        return binding.root
    }

    //-------------BUTTON CLICK----------

    private fun onButtonMap(){
        binding.imageButtonNavigationMap.setOnClickListener {
            // Creates an Intent that will navigate on place
            val latString=viewModel.detailPlace.value!!.lat.toString()
            val lngString=viewModel.detailPlace.value!!.lng.toString()

            val gmmIntentUri = Uri.parse("google.navigation:q=$latString,$lngString")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }


}
