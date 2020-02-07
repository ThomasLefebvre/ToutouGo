package fr.thomas.lefebvre.toutougo.ui.place

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentPlaceBinding

class PlaceFragment : Fragment() {

    private lateinit var binding:FragmentPlaceBinding

    private lateinit var viewModel: PlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_place,container,false)



        onClickAddPlace()


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlaceViewModel::class.java)
        // TODO: Use the ViewModel
    }


    // ---------------- CLICK ON BUTTON -----------------------------

    private fun onClickAddPlace(){
        binding.buttonAddPlace.setOnClickListener {
            view!!.findNavController().navigate(R.id.actionCreatePlace)
        }
    }

}
