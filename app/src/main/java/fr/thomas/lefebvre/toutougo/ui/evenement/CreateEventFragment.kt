package fr.thomas.lefebvre.toutougo.ui.evenement


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.libraries.places.api.model.Place

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentCreateEventBinding
import fr.thomas.lefebvre.toutougo.ui.place.PlaceViewModel
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CreateEventFragment : Fragment(){


    private lateinit var binding:FragmentCreateEventBinding
    private lateinit var viewModel:PlaceViewModel


    var cal = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      binding=DataBindingUtil.inflate(inflater,R.layout.fragment_create_event,container,false)

        viewModel=ViewModelProviders.of(activity!!).get(PlaceViewModel::class.java)

        binding.lifecycleOwner = activity

        binding.viewModel=viewModel

        Log.d("EVENT",viewModel.namePlaceEvent.value.toString())

        onClickPickAddress()

        onClickDatePicker()

        onClickTimePicker()

        setSpinnerInt()

    viewModel.maxCustomer.observe(this,androidx.lifecycle.Observer { maxCustomer->
        if(maxCustomer!=null){
            setSpinnerInt()
        }
    })
        binding.lifecycleOwner=this


        return binding.root
    }

    override fun onResume() {
        Log.d("EVENT","on resume")
        setSpinnerInt()
        super.onResume()
    }

    override fun onDestroy() {
        Log.d("EVENT","on destroy")
        super.onDestroy()
    }

    //--------------- ON CLICK METHOD --------------

    private fun onClickPickAddress(){
        binding.buttonAddAddress.setOnClickListener {
            viewModel.isPlaceOrEvent.value=getString(R.string.event)
            view!!.findNavController().navigate(R.id.action_createEventFragment_to_mapFragment)

        }
    }

    // -------------- DATE PICKER ----------------


    private fun onClickDatePicker(){

        val dateListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                viewModel.dateEvent.value=cal.timeInMillis
            }

        binding.buttonAddDate.setOnClickListener {
            DatePickerDialog(requireContext(),
                dateListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    // -------------- TIME PICKER ----------------

    private fun onClickTimePicker(){

        val timeListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> cal.set(Calendar.HOUR_OF_DAY,hourOfDay)
            cal.set(Calendar.MINUTE,minute)
               viewModel.hourEvent.value=hourOfDay
                viewModel.minuteEvent.value=minute


            }

        binding.buttonAddTime.setOnClickListener {
            TimePickerDialog(requireContext(),
                timeListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),true).show()
        }
    }


    //  ---------           SET SPINNER LIST AND ADAPTER          --------------

    private fun setSpinnerInt() {
        val adapter =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_menu, viewModel.listInt)
        binding.filledExposedDropdownMaxCustomer.setAdapter(adapter)

    }
}
