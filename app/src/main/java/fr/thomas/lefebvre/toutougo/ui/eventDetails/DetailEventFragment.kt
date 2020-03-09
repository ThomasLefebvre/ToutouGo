package fr.thomas.lefebvre.toutougo.ui.eventDetails


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.database.model.Participation
import fr.thomas.lefebvre.toutougo.databinding.FragmentDetailEventBinding
import fr.thomas.lefebvre.toutougo.ui.place.MainViewModel
import fr.thomas.lefebvre.toutougo.ui.presentation.WelcomeViewModel
import fr.thomas.lefebvre.toutougo.ui.userDetail.DetailUserFragment
import kotlinx.android.synthetic.main.alert_dialog_save.view.*

/**
 * A simple [Fragment] subclass.
 */
class DetailEventFragment : Fragment() {

    private lateinit var binding: FragmentDetailEventBinding

    private lateinit var viewModel: MainViewModel

    private lateinit var viewModelWelcome: WelcomeViewModel

    private lateinit var adapterParticpant: ParticipantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_event, container, false)
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        viewModelWelcome = ViewModelProviders.of(activity!!).get(WelcomeViewModel::class.java)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        viewModel.getParticipationCurrentUser()//get if current user particip of this event

        viewModel.getParticipationOfEvent()

        clicDetailsCreator()

        clickRejoinEvent()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapterParticpant = ParticipantAdapter(ParticipantListener {//init adapter with click listener
            onClickDetailUser(it)
        })
        binding.recyclerViewParticipants.adapter=adapterParticpant

        viewModel.listParticipation.observe(this, Observer { participants->
            adapterParticpant.submitList(participants)

        })

        super.onViewCreated(view, savedInstanceState)
    }


    override fun onResume() {
        viewModel.getUserCreator()
        super.onResume()
    }

    override fun onDestroy() {
        viewModel.listParticipation.value=null
        viewModel.userCreatorPhotoUrl.value=null
        super.onDestroy()
    }

    //--------------- ON CLICK BUTTON ------------------

    private fun clicDetailsCreator() {
        binding.imageButtonInfoCreator.setOnClickListener {
            viewModel.detailUser.value = viewModel.detailEvent.value!!.uidCreator
            fragmentDetailUser()
        }
    }

    private fun onClickDetailUser(participation: Participation) {
        viewModel.detailUser.value = participation.uidUser
        fragmentDetailUser()
    }

    private fun clickRejoinEvent() {
        binding.buttonRejoinEvent.setOnClickListener {
            if (viewModel.currentUserIsParticipe.value == true) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.particpe_of_event),
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            } else {
                rejoinEvent()
            }

        }
    }


    //--------------FRAGMENT DETAIL USER---------------

    private fun fragmentDetailUser() {

        val detailFragment = DetailUserFragment()
        val fragmentManager = getFragmentManager()
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
            .add(R.id.nav_host_fragment, detailFragment)
            .addToBackStack("detailUser")
        fragmentTransaction.commit()

    }

    //----------REJOIN EVENT -------

    private fun rejoinEvent() {
        if (viewModel.detailEvent.value!!.maxCustomer > viewModel.detailEvent.value!!.numberCustomer) {
            alertDialogConfirm()
        } else {//if customer is already max
            Snackbar.make(
                requireView(),
                getString(R.string.event_completed),
                Snackbar.LENGTH_SHORT
            )
                .show()
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


            viewModel.createParticipation(viewModelWelcome.photoUserUrl.value!!)//create participation if click on confirm
            viewModel.updateNumberUserEvent()
            viewModel.getParticipationOfEvent()

            mAlertDialog.dismiss()
        }

        mDialog.buttonNo.setOnClickListener {

            mAlertDialog.dismiss()
        }

    }
}
