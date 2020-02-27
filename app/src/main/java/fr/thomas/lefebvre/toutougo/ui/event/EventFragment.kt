package fr.thomas.lefebvre.toutougo.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.database.model.Event
import fr.thomas.lefebvre.toutougo.databinding.FragmentEventBinding
import fr.thomas.lefebvre.toutougo.ui.dashboard.DashBoardViewModel
import fr.thomas.lefebvre.toutougo.ui.place.PlaceAdapter
import fr.thomas.lefebvre.toutougo.ui.place.PlaceListener
import fr.thomas.lefebvre.toutougo.ui.place.PlaceViewModel

class EventFragment : Fragment() {

    private lateinit var viewModel: PlaceViewModel

    private lateinit var viewModelLocation:DashBoardViewModel

    private lateinit var adapter:EventAdapter

    private lateinit var binding:FragmentEventBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(activity!!).get(PlaceViewModel::class.java)

        viewModelLocation=ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_event,container,false)

        binding.lifecycleOwner = activity

        onClickCreateEvent()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getEvent(
            viewModelLocation.lastLatitute.value!!,
            viewModelLocation.lastLongitude.value!!
        )




        adapter = EventAdapter(
            viewModelLocation.lastLatitute.value!!,
            viewModelLocation.lastLongitude.value!!,
            EventListener {
                onClickDetailsEvent(it)
            })

        viewModel.listEvent.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.recyclerViewProxEvent.adapter = adapter
    }





    // ---------------- CLICK ON BUTTON -----------------------------

    private fun onClickCreateEvent(){
        binding.buttonCreateEvent.setOnClickListener {
            view!!.findNavController().navigate(R.id.action_evenementFragment_to_createEventFragment)
        }
    }


    private fun onClickDetailsEvent(event: Event) {
        Toast.makeText(requireContext(),event.descriptionEvent,Toast.LENGTH_LONG).show()

    }
}