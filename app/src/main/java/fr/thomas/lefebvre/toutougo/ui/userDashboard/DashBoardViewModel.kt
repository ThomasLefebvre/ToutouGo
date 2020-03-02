package fr.thomas.lefebvre.toutougo.ui.userDashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.thomas.lefebvre.toutougo.database.helper.DogHelper
import fr.thomas.lefebvre.toutougo.database.model.Dog
import kotlinx.coroutines.*

class DashBoardViewModel : ViewModel() {


    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    private val currentUser = FirebaseAuth.getInstance().currentUser


    // -------------------- VARIABLE FOR DOG INFOS ------------------------
    val listDog = MutableLiveData<ArrayList<Dog>>()
    private val dogHelper = DogHelper()
    val uidDog = MutableLiveData<String>()
    val nameDog = MutableLiveData<String>()
    val heightDog = MutableLiveData<String>()
    val ageDog = MutableLiveData<String>()
    val sexeDog = MutableLiveData<String>()
    val raceDog = MutableLiveData<String>()
    val descriptionDog = MutableLiveData<String>()
    val photoDogUrl = MutableLiveData<String>().apply { value = "noPhoto" }
    val listIntDog = ArrayList<String>()
    val uidUserDog = MutableLiveData<String>()
    val refPhoto = MutableLiveData<String>()


    // -------------------- VARIABLE LOCATION ------------------------


    private val _lastLatitude = MutableLiveData<Double>()
    val lastLatitute: LiveData<Double>
        get() = _lastLatitude

    private val _lastLongitude = MutableLiveData<Double>()
    val lastLongitude: LiveData<Double>
        get() = _lastLongitude


    init {
        getDogUser()
        initListSpinner()
        Log.d("MAP", lastLatitute.value.toString())
    }


    // -------------------- UPDATE DOG   ------------------------

    fun initListSpinner() {
        //init list int dog
        for (i in 0..99) {//init list age
            listIntDog.add(i.toString())
        }
        Log.d("DEBUG", "init spinner in viewmodel")
    }

    //CHECK IF HAVE ALL INFORMATION DOG
    fun checkAllInfosDog(): Boolean {
        return (nameDog.value != null
                && ageDog.value != null
                && heightDog.value != null
                && sexeDog.value != null
                && descriptionDog.value != null
                && raceDog.value != null)
    }

    //UPDATE DOG
    fun updateDog() {
        val dog = Dog(
            uidDog.value!!,
            nameDog.value!!,
            heightDog.value!!.toInt(),
            ageDog.value!!.toInt(),
            sexeDog.value!!,
            raceDog.value!!,
            photoDogUrl.value.toString(),
            descriptionDog.value!!,
            uidUserDog.value!!,
            refPhoto.value!!

        )
        uiScope.launch {
            createDogInDatabase(dog)
        }
    }

    private suspend fun createDogInDatabase(dog: Dog) {
        withContext(Dispatchers.IO) {
            dogHelper.createDog(dog)
        }
    }

    //CREATE DOG
    fun updatePhotoDog() {
        uiScope.launch {
            updatePhotoDogInDatabase(uidDog.value!!, photoDogUrl.value!!)
        }
    }

    private suspend fun updatePhotoDogInDatabase(uidDog: String, photoUrl: String) {
        withContext(Dispatchers.IO) {
            dogHelper.updatePhotoDog(uidDog, photoUrl)
        }
    }

    // -------------------- GET DOG FROM FIRESTORE FOR RECYCLER VIEW  ------------------------
    fun getDogUser() {
        uiScope.launch {
            getDodUserFromUserFireStore()
        }
    }

    private suspend fun getDodUserFromUserFireStore() {
        withContext(Dispatchers.IO) {
            dogHelper.getAllDogUser(currentUser!!.uid).addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    Log.d("DEBUG", "No dogs")
                } else {
                    val list = ArrayList<Dog>()
                    for (document in documents) {
                        val dogFirebase = document.toObject(Dog::class.java)
                        list.add(dogFirebase)

                    }
                    listDog.value = list
                    Log.d("DEBUG", listDog.value!![0].height.toString())


                }

            }
        }
    }


    // -------------------- SET LOCATION USER  ------------------------

    fun setLocation(latitude: Double,longitude:Double) {
        _lastLatitude.value = latitude
        _lastLongitude.value = longitude
        Log.d("LOCATION", lastLatitute.value.toString() + " " + lastLongitude.value.toString())
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}