package fr.thomas.lefebvre.toutougo.ui.presentation


import android.app.Activity
import android.app.VoiceInteractor
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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentUserInfosBinding
import fr.thomas.lefebvre.toutougo.ui.MainActivity
import fr.thomas.lefebvre.toutougo.utils.setBitmapFromView
import kotlinx.android.synthetic.main.fragment_dog_infos.*
import kotlinx.android.synthetic.main.fragment_user_infos.*
import java.io.ByteArrayOutputStream


class UserInfosFragment : Fragment() {

    private lateinit var viewModel: WelcomeViewModel
    lateinit var binding: FragmentUserInfosBinding


    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    companion object {
        const val CODE_CHOOSE_PHOTO = 100
        const val pathStringUser = "usersPhoto/"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_infos, container, false)

        viewModel = ViewModelProviders.of(activity!!).get(WelcomeViewModel::class.java)

        Log.d("DEBUG","init spinner in onCreateView")
        onClickOnNext()
        onClickAddPhoto()
        onClickButtonEdit()

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        binding.viewModel = viewModel
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onResume() {
        setSpinnerAge()
        setSpinnerSexe()
        Log.d("DEBUG","init spinner in onResume")
        super.onResume()
    }






    //  ---------           ACTIVITY RESULT FOR PICK PHOTO                --------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CODE_CHOOSE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    imageViewPhotoProfil.setImageURI(data!!.data)

                    filePath = data.data
                    uploadPhoto()


                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //  ---------           PICK PHOTO INTENT                --------------

    private fun pickPhoto() {
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(
            intentGallery,
            CODE_CHOOSE_PHOTO
        )
    }

    //  ---------           SAVE DATA FIRESTORE                --------------

    private fun uploadPhoto() {
        if (filePath != null) {
            val ref = storageReference!!.child(pathStringUser + viewModel.currentUser.uid)
            val bitmap = binding.imageViewPhotoProfil.setBitmapFromView()
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
                    viewModel.photoUserUrl.value = task.result.toString()
                } else {
                    Snackbar.make(requireView(),"Erreur",Snackbar.LENGTH_LONG).show()
                }
            }

        }
    }


    //  ---------           SET SPINNER LIST AND ADAPTER          --------------

    private fun setSpinnerAge() {
        val adapter =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_menu, viewModel.listInt)
        binding.filledExposedDropdown.setAdapter(adapter)
    }

    private fun setSpinnerSexe() {
        val res = resources
        val listSexe = res.getStringArray(R.array.sex_type)
        val adapter =
            ArrayAdapter<String>(requireContext(), R.layout.dropdown_menu, listSexe)
        binding.filledExposedDropdownCivilite.setAdapter(adapter)
    }

    //  ---------           BUTTON CLICK                 --------------

    private fun onClickOnNext() {
        binding.floatingActionButtonNextUserInfos.setOnClickListener {

            if (viewModel.checkAllInfos()) {
                viewModel.createUser()
                view!!.findNavController().navigate(R.id.action_userInfos_to_dogInfosFragment)
            } else {
                Snackbar.make(requireView(),getString(R.string.complete_infos), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun onClickAddPhoto() {
        binding.buttonAddPhotoUser.setOnClickListener {
            pickPhoto()
        }
    }

    private fun onClickButtonEdit(){
        binding.floatingActionButtonEditUser.setOnClickListener {
            if (viewModel.checkAllInfos()) {
                viewModel.createUser()
                view!!.findNavController().navigate(R.id.action_userInfosFragment_to_welcomeFragment)
            } else {
                Snackbar.make(requireView(),getString(R.string.complete_infos), Snackbar.LENGTH_SHORT)
                    .show()
            }

        }
    }

}
