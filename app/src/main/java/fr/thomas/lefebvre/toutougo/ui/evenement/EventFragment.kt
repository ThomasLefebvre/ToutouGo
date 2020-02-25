package fr.thomas.lefebvre.toutougo.ui.evenement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentEventBinding
import fr.thomas.lefebvre.toutougo.ui.place.PlaceViewModel

class EventFragment : Fragment() {

    private lateinit var viewModel: PlaceViewModel

    private lateinit var binding:FragmentEventBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(activity!!).get(PlaceViewModel::class.java)

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_event,container,false)

        binding.lifecycleOwner = activity

        onClickCreateEvent()

        return binding.root
    }





    // ---------------- CLICK ON BUTTON -----------------------------

    private fun onClickCreateEvent(){
        binding.buttonCreateEvent.setOnClickListener {
            view!!.findNavController().navigate(R.id.action_evenementFragment_to_createEventFragment)
        }
    }
}