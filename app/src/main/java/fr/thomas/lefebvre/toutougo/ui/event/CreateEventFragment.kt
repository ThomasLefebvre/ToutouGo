package fr.thomas.lefebvre.toutougo.ui.event


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentCreateEventBinding
import fr.thomas.lefebvre.toutougo.ui.place.MainViewModel
import kotlinx.android.synthetic.main.alert_dialog_save.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CreateEventFragment : Fragment() {


    private lateinit var binding: FragmentCreateEventBinding
    private lateinit var viewModel: MainViewModel


    var cal = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_event, container, false)

        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        binding.lifecycleOwner = activity

        binding.viewModel = viewModel

        Log.d("EVENT", viewModel.namePlaceEvent.value.toString())

        onClickPickAddress()

        onClickDatePicker()

        onClickTimePicker()

        setSpinnerInt()

        onClickSave()

        viewModel.maxCustomer.observe(this, androidx.lifecycle.Observer { maxCustomer ->
            if (maxCustomer != null) {
                setSpinnerInt()
            }
        })
        binding.lifecycleOwner = this


        return binding.root
    }

    override fun onResume() {

        Log.d("EVENT", "on resume")
        setSpinnerInt()
        super.onResume()
    }

    override fun onDestroy() {

        Log.d("EVENT", "on destroy")
        super.onDestroy()
    }

    //--------------- ON CLICK METHOD --------------

    private fun onClickPickAddress() {

        binding.buttonAddAddress.setOnClickListener {
            viewModel.isPlaceOrEvent.value = getString(R.string.event)
            view!!.findNavController().navigate(R.id.action_createEventFragment_to_mapFragment)

        }
    }

    private fun onClickSave() {
        binding.floatingActionButtonSaveEvent.setOnClickListener {
            if(viewModel.checkAllInfosEvent()){//if all info completed
                alertDialogConfirm()//launch alert dialog confirm
            } else {
                Snackbar.make(//snack bar alert
                    requireView(),
                    getString(R.string.complete_infos),
                    Snackbar.LENGTH_SHORT
                )
                    .show()}
        }


    }

    // -------------- DATE PICKER ----------------


    private fun onClickDatePicker() {

        val dateListener =//init date picker
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cal.set(Calendar.HOUR,0)
                cal.set(Calendar.HOUR_OF_DAY,0)
                cal.set(Calendar.MINUTE,0)
                cal.set(Calendar.SECOND,0)
                cal.set(Calendar.MILLISECOND,0)
                viewModel.dateEvent.value = (cal.timeInMillis)//get date pick
            }

        binding.buttonAddDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    // -------------- TIME PICKER ----------------

    private fun onClickTimePicker() {

        val timeListener =//init time picker
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                viewModel.hourEvent.value = hourOfDay//get hour pick
                viewModel.minuteEvent.value = minute//get minute pick


            }

        binding.buttonAddTime.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                timeListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true
            ).show()
        }
    }


    //  ---------           SET SPINNER LIST AND ADAPTER          --------------

    private fun setSpinnerInt() {//init spinner for max customer

        val adapter =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_menu, viewModel.listInt)
        binding.filledExposedDropdownMaxCustomer.setAdapter(adapter)

    }


    //-------------ALERT DIALOG CONFIRM----------

    private fun alertDialogConfirm() {//init alert dialog for create event

        val mDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_save, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialog)
        val mAlertDialog = mBuilder.show()
        mDialog.buttonYes.setOnClickListener {

            viewModel.createEvent()//create event
            viewModel.clearInfosEvent()//clear infos for next creation
            view!!.findNavController().navigate(R.id.action_createEventFragment_to_evenementFragment)//navigate to event fragment


            mAlertDialog.dismiss()//dismiss alert dialog
        }
        mDialog.buttonNo.setOnClickListener {


            mAlertDialog.dismiss()//dismiss alert dialog

        }

    }
}
