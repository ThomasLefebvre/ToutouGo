package fr.thomas.lefebvre.toutougo.ui.place


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.databinding.FragmentPhotoPlaceBinding
import fr.thomas.lefebvre.toutougo.ui.dashboard.DogAdapter
import fr.thomas.lefebvre.toutougo.ui.dashboard.DogListener
import fr.thomas.lefebvre.toutougo.ui.edit.EditFragment
import fr.thomas.lefebvre.toutougo.utils.setBitmapFromView
import kotlinx.android.synthetic.main.fragment_dog_infos.*
import kotlinx.android.synthetic.main.fragment_photo_place.*
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 */
class PhotoPlaceFragment : Fragment() {

    private lateinit var binding: FragmentPhotoPlaceBinding

    private lateinit var viewModel: PlaceViewModel

    private lateinit var viewModelPhoto: PhotoPlaceViewModel

    private lateinit var adapter: PhotoPlaceAdapter


    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    companion object {
        const val CODE_CHOOSE_PHOTO = 200
        const val pathString = "placesPhoto/"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_photo_place, container, false)


        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference




        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(PlaceViewModel::class.java)

        viewModelPhoto = ViewModelProviders.of(this).get(PhotoPlaceViewModel::class.java)
        binding.lifecycleOwner = activity
        binding.viewModel = viewModelPhoto

        adapter = PhotoPlaceAdapter(PhotoPlaceListener { photo ->
            deletePhoto(photo.uidPhoto)

        })

        viewModelPhoto.uidPlace.value = viewModel.uidPlace.value


        viewModelPhoto.listPhotoPlace.observe(this, Observer {

                adapter.submitList(it)


        })
        val layoutManger = GridLayoutManager(requireContext(), 2)

        binding.recyclerViewPhotoPlace.adapter = adapter

        binding.recyclerViewPhotoPlace.layoutManager = layoutManger


        onClickAddPhoto()
        onClickSave()

    }

    override fun onResume() {

        Log.d("DEBUG", "On resume")
        super.onResume()
    }


    // ---------------- CLICK ON BUTTON -----------------------------

    private fun onClickAddPhoto() {
        binding.buttonSelectPhotoPlace.setOnClickListener {
            pickPhoto()
        }
    }

    private fun deletePhoto(uidPhoto:String) {

        viewModelPhoto.deletePhotoPlace(uidPhoto)


    }

    private fun onClickSave(){
        binding.floatingActionButtonSavePlace.setOnClickListener {
            if (viewModelPhoto.listPhotoPlace.value!=null){
                viewModelPhoto.updatePhotoMainPlace()
                viewModel.clearPlaceAfterSave()
                Snackbar.make(requireView(), getString(R.string.place_delais), Snackbar.LENGTH_LONG).show()
                view!!.findNavController().navigate(R.id.action_photoPlaceFragment_to_placeFragment)

            }
            else{
                Snackbar.make(requireView(), getString(R.string.alert_photo_place_create), Snackbar.LENGTH_LONG).show()
            }
        }

    }


    //  ---------           Pick photo Intent                --------------

    private fun pickPhoto() {
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(
                intentGallery,
                CODE_CHOOSE_PHOTO

        )
    }

    //  ---------           UPLOAD PHOTO TO STORAGE                --------------
    private fun uploadPhoto() {
        if (filePath != null) {
            val ref: StorageReference = storageReference!!.child(pathString + System.currentTimeMillis().toString())


            val bitmap = binding.imageViewPhotoPlace.setBitmapFromView()
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
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
                    viewModelPhoto.urlPhotoPlace.value = task.result.toString()

                    viewModelPhoto.createPhotoPlace()
                    viewModelPhoto.getPhoto()

                } else {
                    Snackbar.make(requireView(), "Erreur", Snackbar.LENGTH_LONG).show()
                }
            }

        }
    }

    //  ---------           Activity result                --------------

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            EditFragment.CODE_CHOOSE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    imageViewPhotoPlace.setImageURI(data!!.data)
                    filePath = data.data
                    uploadPhoto()

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }




}
