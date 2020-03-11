package fr.thomas.lefebvre.toutougo.ui.toolsUser


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentToolsUserBinding
import fr.thomas.lefebvre.toutougo.ui.login.LoginActivity
import fr.thomas.lefebvre.toutougo.ui.presentation.WelcomeViewModel
import kotlinx.android.synthetic.main.alert_dialog_save.view.*

/**
 * A simple [Fragment] subclass.
 */
class ToolsUserFragment : Fragment() {

    private lateinit var binding: FragmentToolsUserBinding

    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tools_user, container, false)

        viewModel = ViewModelProviders.of(activity!!).get(WelcomeViewModel::class.java)

        viewModel.isDelete.observe(this, Observer { isDelete ->
            if (isDelete == true) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
        })

        clickOnDeleteAccount()
        clickOnLogOutAccount()


        return binding.root
    }


    //--------------BUTTON CLICK-----------------

    private fun clickOnDeleteAccount() {
        binding.buttonDeleteAccount.setOnClickListener {
            alertDialogDelete()
        }

    }

    private fun clickOnLogOutAccount() {
        binding.buttonLogOut.setOnClickListener {
            alertDialogLogOut()
        }

    }


    //-------------ALERT DIALOG CONFIRM----------

    private fun alertDialogDelete() {

        val mDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_save, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialog)
        val mAlertDialog = mBuilder.show()
        mDialog.buttonYes.setOnClickListener {

            viewModel.deleteAccount(requireContext())

            mAlertDialog.dismiss()
        }

        mDialog.buttonNo.setOnClickListener {

            mAlertDialog.dismiss()
        }

    }

    private fun alertDialogLogOut() {

        val mDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_save, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialog)
        val mAlertDialog = mBuilder.show()
        mDialog.buttonYes.setOnClickListener {

            AuthUI.getInstance()//Log out account from Firebase Auth UI
                .signOut(requireContext())
                .addOnCompleteListener {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))//Launch login activity after log out
                    requireActivity().finish()
                }

            mAlertDialog.dismiss()
        }

        mDialog.buttonNo.setOnClickListener {

            mAlertDialog.dismiss()
        }

    }


}
