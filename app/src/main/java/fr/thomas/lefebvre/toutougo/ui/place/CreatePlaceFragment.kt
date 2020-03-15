package fr.thomas.lefebvre.toutougo.ui.place


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentCreatePlaceBinding
import kotlinx.android.synthetic.main.alert_dialog_save.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CreatePlaceFragment : Fragment() {

    private lateinit var binding: FragmentCreatePlaceBinding

    private lateinit var viewModel: MainViewModel

    companion object {
        //CODE REQUESTS
        private const val AUTOCOMPLETE_REQUEST = 100

    }


    // ------------------ LIFE CYCLE METHOD ---------------------
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_create_place,
            container,
            false
        )

        Places.initialize(
            requireContext(),
            getString(R.string.map_api_key)
        )//INIT PLACE SDK FOR GET ADDRESS


        onButtonSelectAddress()
        onButtonPickAddress()
        onClickNext()



        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = activity
    }


    // ------------------ GOOGLE AUTOCOMPLETE PLACE SELECTED  ---------------------

    private fun initAutoCompleteIntent() {

        val fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG
        )//init the fields return from autocomplete
        val intentAutocomplete =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(requireContext())
        startActivityForResult(intentAutocomplete, AUTOCOMPLETE_REQUEST)//init auto complete and start activity for result
    }


    // ------------------ ACTIVITY RESULT FOR SELECT ADDRESS  ---------------------


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

            AUTOCOMPLETE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val place = Autocomplete.getPlaceFromIntent(data!!)//get result of auto complete
                    viewModel.adressPlace.value = place.address//get address
                    viewModel.latPlace.value = place.latLng!!.latitude//get lat
                    viewModel.lngPlace.value = place.latLng!!.longitude//get lng

                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {//if error in request
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Log.i("DEBUG_AUTOCOMPLETE", status.statusMessage!!)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // ------------------ ON BUTTON CLICk   --------------------------------------

    private fun onButtonSelectAddress() {
        binding.buttonAddAddress.setOnClickListener {
            initAutoCompleteIntent()
        }
    }

    private fun onButtonPickAddress() {
        binding.buttonPickAddress.setOnClickListener {
            viewModel.isPlaceOrEvent.value = getString(R.string.place)
            view!!.findNavController().navigate(R.id.action_createPlaceFragment_to_mapFragment)
        }
    }

    private fun onClickNext() {
        binding.floatingActionButtonNextPlace.setOnClickListener {
            if (viewModel.checkAllInfosPlace()) {

                alertDialogConfirm()

            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.complete_infos),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }

        }
    }

    //-------------ALERT DIALOG CONFIRM----------

    private fun alertDialogConfirm() {

        val mDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_save, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialog)
        val mAlertDialog = mBuilder.show()
        mDialog.buttonYes.setOnClickListener {

            viewModel.createPlace()
            view!!.findNavController().navigate(R.id.actionPhotoPlace)
            mAlertDialog.dismiss()
        }

        mDialog.buttonNo.setOnClickListener {

            mAlertDialog.dismiss()
        }

    }


}
