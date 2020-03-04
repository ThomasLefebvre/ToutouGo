package fr.thomas.lefebvre.toutougo.ui.edit


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentEditBinding
import fr.thomas.lefebvre.toutougo.ui.presentation.WelcomeViewModel
import fr.thomas.lefebvre.toutougo.ui.userDashboard.DashBoardViewModel
import fr.thomas.lefebvre.toutougo.utils.setBitmapFromView
import kotlinx.android.synthetic.main.alert_dialog_save.view.*
import kotlinx.android.synthetic.main.fragment_dog_infos.*
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 */
class EditFragment : Fragment() {

    lateinit var viewModel: DashBoardViewModel

    private lateinit var mViewModelWelcome: WelcomeViewModel

    lateinit var binding: FragmentEditBinding


    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    companion object {
        const val CODE_CHOOSE_PHOTO = 200
        const val pathStringDog = "dogsPhoto/"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)

        mViewModelWelcome=ViewModelProviders.of(activity!!).get(WelcomeViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false)

        Log.d("DEBUG", viewModel.refPhoto.value!!)

        onClickAddPhoto()
        onClickNext()
        onClickDelete()

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onResume() {
        setSpinnerInt()
        setSpinnerRace()
        setSpinnerSexe()
        super.onResume()

    }


    //  ---------           SET SPINNER LIST AND ADAPTER          --------------

    private fun setSpinnerInt() {
        val adapter =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_menu, viewModel.listIntDog)
        binding.filledExposedDropdownAgeDog.setAdapter(adapter)
        binding.filledExposedDropdownHeight.setAdapter(adapter)
    }

    private fun setSpinnerSexe() {
        val res = resources
        val listSexe = res.getStringArray(R.array.dog_sex)
        val adapter =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_menu, listSexe)
        binding.filledExposedDropdownSexeDog.setAdapter(adapter)
    }

    private fun setSpinnerRace() {
        val res = resources
        val listRace = res.getStringArray(R.array.race_array)
        val adapter =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_menu, listRace)
        binding.filledExposedDropdownRace.setAdapter(adapter)
    }


    //  ---------           Activity result                --------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CODE_CHOOSE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    imageViewPhotoDog.setImageURI(data!!.data)
                    filePath = data.data
                    uploadPhoto()


                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    //  ---------           Pick photo Intent                --------------

    private fun pickPhoto() {
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(
            intentGallery,
            CODE_CHOOSE_PHOTO

        )
    }

    //  ---------           SAVE PHOTO IN FIRESTORE                --------------

    private fun uploadPhoto() {
        if (filePath != null) {
            val ref: StorageReference
            val refPhoto = viewModel.refPhoto.value.toString()
            if (refPhoto != "") {
                ref =
                    storageReference!!.child(pathStringDog + refPhoto)
            } else {
                ref =
                    storageReference!!.child(pathStringDog + System.currentTimeMillis().toString())
            }

            val bitmap = binding.imageViewPhotoDog.setBitmapFromView()
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val data = baos.toByteArray()

            var uploadTask = ref.putBytes(data)
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModel.photoDogUrl.value = task.result.toString()
                    viewModel.updatePhotoDog()


                } else {
                    Snackbar.make(requireView(), "Erreur", Snackbar.LENGTH_LONG).show()
                }
            }

        }
    }

    //  ---------           BUTTON CLICK                --------------

    private fun onClickAddPhoto() {
        binding.buttonAddPhotoDog.setOnClickListener {
            pickPhoto()
        }
    }

    private fun onClickNext() {
        binding.floatingActionButtonNextInfos.setOnClickListener {

            if (viewModel.checkAllInfosDog()) {


                viewModel.updateDog()//update dog in database firestore
                view!!.findNavController()
                    .navigate(R.id.action_editFragment_to_welcomeFragment)

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

    private fun onClickDelete() {
        binding.floatingActionButtonDelete.setOnClickListener {
            alertDialogConfirm()
        }
    }

    //------------------ ALERTE DIALOG ----------------------


    private fun alertDialogConfirm() {

        val mDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_save, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialog)
        val mAlertDialog = mBuilder.show()
        mDialog.buttonYes.setOnClickListener {

            viewModel.deleteDog()
            mViewModelWelcome.updateNumberDogUserLess()

            view!!.findNavController()
                .navigate(R.id.action_editFragment_to_welcomeFragment)
            mAlertDialog.dismiss()
        }
        mDialog.buttonNo.setOnClickListener {


            mAlertDialog.dismiss()

        }

    }
}
