package fr.thomas.lefebvre.toutougo.ui.place

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.thomas.lefebvre.toutougo.database.helper.PlaceHelper
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.database.model.PhotoPlace
import fr.thomas.lefebvre.toutougo.database.model.Place
import kotlinx.coroutines.*

class PlaceViewModel : ViewModel() {


    //------------- VARIABLE FOR COROUTINES ------------------

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)



    //------------- VARIABLE FOR CREATE PLACE ---------------

    val uidPlace=MutableLiveData<String>()
    val namePlace=MutableLiveData<String>()
    val descriptionPlace=MutableLiveData<String>()
    val latPlace=MutableLiveData<Double>()
    val lngPlace=MutableLiveData<Double>()
    val adressPlace=MutableLiveData<String>().apply { value="" }


    private val placeHelper=PlaceHelper()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    val listPlace = MutableLiveData<ArrayList<Place>>()


    init {
        getPlace()
    }


    //CREATE PLACE
    fun createPlace() {
        if(uidPlace.value==null){
            uidPlace.value=currentUser!!.uid+System.currentTimeMillis().toString()
        }
        val place = Place(
                uidPlace.value!!,
                namePlace.value!!,
                descriptionPlace.value!!,
                adressPlace.value!!,
                latPlace.value!!,
                lngPlace.value!!


        )
        uiScope.launch {
            createDogInDatabase(place)
        }
    }


    private suspend fun createDogInDatabase(place: Place) {
        withContext(Dispatchers.IO) {
            placeHelper.createPlace(place)
        }
    }



    fun checkAllInfosPlace(): Boolean {
        return (namePlace.value!= null
                && descriptionPlace.value != null
                && latPlace.value != null
                && lngPlace.value != null
               )
    }

    // -------------------- GET PLACE FROM FIRESTORE FOR RECYCLER VIEW  ------------------------
    fun getPlace() {
        uiScope.launch {
            getPlaceFromFireStore()


        }
    }

    private suspend fun getPlaceFromFireStore() {
        withContext(Dispatchers.IO) {
            placeHelper.getAllPlace().addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    Log.d("DEBUG", "No places")
                } else {
                    val list = ArrayList<Place>()
                    for (document in documents) {
                        val place = document.toObject(Place::class.java)
                        list.add(place)

                    }
                    listPlace.value = list
                    Log.d("DEBUG", "YES places")
                }

            }
        }
    }


}

