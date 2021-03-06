package fr.thomas.lefebvre.toutougo.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.database.model.Event
import fr.thomas.lefebvre.toutougo.databinding.FragmentEventBinding
import fr.thomas.lefebvre.toutougo.ui.userDashboard.DashBoardViewModel
import fr.thomas.lefebvre.toutougo.ui.place.MainViewModel

class EventFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var viewModelLocation:DashBoardViewModel

    private lateinit var adapter:EventAdapter

    private lateinit var binding:FragmentEventBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModelLocation=ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_event,container,false)

        binding.lifecycleOwner = activity

        onClickCreateEvent()

        return binding.root
    }

    override fun onResume() {
        viewModel.getEvent(//get event for display in recycler view
            viewModelLocation.lastLatitute.value!!,
            viewModelLocation.lastLongitude.value!!
        )
        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = EventAdapter(//init adapter with location view from view model
            viewModelLocation.lastLatitute.value!!,
            viewModelLocation.lastLongitude.value!!,
            EventListener {
                onClickDetailsEvent(it)
            })

        binding.recyclerViewProxEvent.adapter = adapter

        viewModel.listEvent.observe(this, Observer {//observe list of fragment

                adapter.submitList(it)//submit list of adapter for display
            if (it.size == 0) {//if no event less 20km
                Snackbar.make(//alert dialog alert for no event proximity
                    requireView(),
                    getString(R.string.no_event),
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }

        })


    }





    // ---------------- CLICK ON BUTTON -----------------------------

    private fun onClickCreateEvent(){
        binding.buttonCreateEvent.setOnClickListener {
            view!!.findNavController().navigate(R.id.action_evenementFragment_to_createEventFragment)

        }
    }


    private fun onClickDetailsEvent(event: Event) {
        viewModel.detailEvent.value=event
        view!!.findNavController().navigate(R.id.action_evenementFragment_to_detailEvent)

    }
}