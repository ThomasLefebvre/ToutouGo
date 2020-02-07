package fr.thomas.lefebvre.toutougo.ui.place

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.thomas.lefebvre.toutougo.database.helper.PhotoPlaceHelper
import fr.thomas.lefebvre.toutougo.database.helper.PlaceHelper
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.database.model.PhotoPlace
import fr.thomas.lefebvre.toutougo.database.model.Place
import kotlinx.coroutines.*

class PhotoPlaceViewModel : ViewModel() {


    //------------- VARIABLE FOR COROUTINES ------------------

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    //------------- INIT ------------------

    init {
        getPhoto()
    }

    //------------- VARIABLE FOR GET PHOTO ---------------

    private val placeHelper=PlaceHelper()
    private val photoPlaceHelper=PhotoPlaceHelper()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    val listPhotoPlace = MutableLiveData<ArrayList<PhotoPlace>>()

    val uidPlace=MutableLiveData<String>()
    val uidPhotoPlace = MutableLiveData<String>()
    val urlPhotoPlace = MutableLiveData<String>()


    // -------------------- CREATE PHOTO PLACE         ------------------------

    //UPDATE DOG
    fun createPhotoPlace() {
        uidPhotoPlace.value=uidPlace.value+System.currentTimeMillis().toString()
        val photoPlace = PhotoPlace(
                uidPhotoPlace.value!!,
                urlPhotoPlace.value!!,
                uidPlace.value!!

        )
        uiScope.launch {
            createPhotoPlaceInFirestore(photoPlace)
        }
    }

    private suspend fun createPhotoPlaceInFirestore(photoPlace: PhotoPlace) {
        withContext(Dispatchers.IO) {
           photoPlaceHelper.createPhotoPlace(photoPlace)
        }
    }



    // -------------------- GET PHOTO PLACE FROM FIRESTORE FOR RECYCLER VIEW  ------------------------
    fun getPhoto() {
        uiScope.launch {
            getPhotoPlaceFromFireStore()
            Log.d("DEBUG", uidPlace.value.toString())

        }
    }

    private suspend fun getPhotoPlaceFromFireStore() {
        withContext(Dispatchers.IO) {
            photoPlaceHelper.getAllPhotoPlace(uidPlace.value!!).addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    Log.d("DEBUG", "No photos")
                } else {
                    val list = ArrayList<PhotoPlace>()
                    for (document in documents) {
                        val photPlace = document.toObject(PhotoPlace::class.java)
                        list.add(photPlace)

                    }
                    listPhotoPlace.value = list
                    Log.d("DEBUG", "YES photos")
                }

            }
        }
    }





}

