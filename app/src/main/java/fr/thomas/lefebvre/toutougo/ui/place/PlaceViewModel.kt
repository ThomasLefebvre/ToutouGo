package fr.thomas.lefebvre.toutougo.ui.place

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.thomas.lefebvre.toutougo.database.helper.PlaceHelper
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.database.model.PhotoPlace
import fr.thomas.lefebvre.toutougo.database.model.Place
import fr.thomas.lefebvre.toutougo.utils.computeDistance
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
    fun getPlace(userLat:Double,userLng:Double) {
        uiScope.launch {
            getPlaceFromFireStore(userLat,userLng)


        }
    }

    private suspend fun getPlaceFromFireStore(userLat:Double,userLng:Double) {
        withContext(Dispatchers.IO) {
            placeHelper.getAllPlace().addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    Log.d("DEBUG", "No places")
                } else {
                    val list = ArrayList<Place>()
                    for (document in documents) {

                        var place = document.toObject(Place::class.java)
                        place=Place(place.uid,place.name,place.description,place.adress,place.lat,place.lng,
                            computeDistance(userLat,userLng,place.lat,place.lng),place.onLine,place.photoUrlMain)

                        list.add(place)

                    }
                    list.sortWith(Comparator<Place> { p1, p2 ->
                        when{
                            p1!!.distance > p2!!.distance -> 1
                            p1.distance == p2.distance -> 0
                            else -> -1
                        }
                    })
                    listPlace.value = list
                    Log.d("DEBUG", "YES places")
                }

            }
        }
    }


}

