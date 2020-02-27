package fr.thomas.lefebvre.toutougo.ui.eventDetails


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentDetailEventBinding
import fr.thomas.lefebvre.toutougo.ui.place.PlaceViewModel
import fr.thomas.lefebvre.toutougo.ui.placeDetails.DetailPlaceFragment
import fr.thomas.lefebvre.toutougo.ui.userDetail.DetailUserFragment

/**
 * A simple [Fragment] subclass.
 */
class DetailEventFragment : Fragment() {

    private lateinit var binding:FragmentDetailEventBinding

    private lateinit var viewModel:PlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_detail_event,container,false)
        viewModel=ViewModelProviders.of(activity!!).get(PlaceViewModel::class.java)

        binding.viewModel=viewModel

        Log.d("DEBUG", "load user creator from firestore ${viewModel.userCreatorPhotoUrl.value}")

        binding.lifecycleOwner = this

        clicDetailsCreator()

        return binding.root
    }


    override fun onResume() {
        viewModel.getUserCreator()
        super.onResume()
    }

    //--------------- ON CLICK BUTTON ------------------

    fun clicDetailsCreator(){
        binding.imageButtonInfoCreator.setOnClickListener {
            viewModel.detailUser.value=viewModel.detailEvent.value!!.uidCreator
            fragmentDetailUser()
        }
    }










    //--------------FRAGMENT DETAIL USER---------------
    private fun fragmentDetailUser() {

        val detailFragment = DetailUserFragment()
        val fragmentManager = getFragmentManager()
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        fragmentTransaction.add(R.id.nav_host_fragment, detailFragment).
            addToBackStack("detailUser")
        fragmentTransaction.commit()


    }
}
