package fr.thomas.lefebvre.toutougo.ui.place

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentPlaceBinding
import fr.thomas.lefebvre.toutougo.ui.dashboard.DashBoardViewModel

class PlaceFragment : Fragment() {

    private lateinit var binding:FragmentPlaceBinding

    private lateinit var viewModel: PlaceViewModel

    private lateinit var viewModelLocation: DashBoardViewModel

    private lateinit var adapter:  PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_place,container,false)

        binding.lifecycleOwner=this

        onClickAddPlace()


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlaceViewModel::class.java)

        viewModelLocation= ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)

        viewModel.getPlace(viewModelLocation.lastLatitute.value!!,viewModelLocation.lastLongitude.value!!)

        adapter= PlaceAdapter(viewModelLocation.lastLatitute.value!!,viewModelLocation.lastLongitude.value!!,PlaceListener {
            //TODO CLICK ON INFOS PLACE
        })

        viewModel.listPlace.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.recyclerViewProxCoin.adapter=adapter
    }


    // ---------------- CLICK ON BUTTON -----------------------------

    private fun onClickAddPlace(){
        binding.buttonAddPlace.setOnClickListener {
            view!!.findNavController().navigate(R.id.actionCreatePlace)
        }
    }

}
