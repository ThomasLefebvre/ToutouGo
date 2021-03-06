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
import com.google.android.material.snackbar.Snackbar
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentPlaceBinding
import fr.thomas.lefebvre.toutougo.ui.userDashboard.DashBoardViewModel


class PlaceFragment : Fragment() {

    private lateinit var binding: FragmentPlaceBinding

    private lateinit var viewModel: MainViewModel

    private lateinit var viewModelLocation: DashBoardViewModel

    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place, container, false)

        binding.lifecycleOwner = activity



        onClickAddPlace()



        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModelLocation = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)


        viewModel.getPlace(
            viewModelLocation.lastLatitute.value!!,
            viewModelLocation.lastLongitude.value!!
        )




        adapter = PlaceAdapter(
            viewModelLocation.lastLatitute.value!!,
            viewModelLocation.lastLongitude.value!!,
            PlaceListener {
                onClickDetailsPlace(it)
            })

        viewModel.listPlace.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
            if (it.size == 0) {//if no place less 20km
                Snackbar.make(
                    requireView(),
                    getString(R.string.no_place),
                    Snackbar.LENGTH_LONG
                )
                    .show()
            }
        })

        binding.recyclerViewProxCoin.adapter = adapter
    }


    // ---------------- CLICK ON BUTTON -----------------------------

    private fun onClickAddPlace() {
        binding.buttonAddPlace.setOnClickListener {
            view!!.findNavController().navigate(R.id.actionCreatePlace)
        }
    }

    private fun onClickDetailsPlace(place: fr.thomas.lefebvre.toutougo.database.model.Place) {
        viewModel.clickDetailPlace(place)
        view!!.findNavController().navigate(R.id.action_placeFragment_to_detailPlaceFragment)
    }


}
