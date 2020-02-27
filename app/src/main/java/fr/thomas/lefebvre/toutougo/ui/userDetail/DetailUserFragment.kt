package fr.thomas.lefebvre.toutougo.ui.userDetail


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentDetailUserBinding
import fr.thomas.lefebvre.toutougo.ui.place.PlaceViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailUserFragment : Fragment() {

    private lateinit var binding:FragmentDetailUserBinding

    private lateinit var viewModel:UserDetailViewModel

    private lateinit var viewModelPlace:PlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_detail_user,container,false)
        viewModel=ViewModelProviders.of(this).get(UserDetailViewModel::class.java)

        viewModelPlace=ViewModelProviders.of(activity!!).get(PlaceViewModel::class.java)

        binding.viewModel=viewModel

        binding.lifecycleOwner=this

        viewModel.getUserDetail(viewModelPlace.detailUser.value!!)

        onClickBack()
        return binding.root
    }


    // -------------- ON CLICK BUTTON ---------------

    private fun onClickBack(){
        binding.imageButtonBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }


}
