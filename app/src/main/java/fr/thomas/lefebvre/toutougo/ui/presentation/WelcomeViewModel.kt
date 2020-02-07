package fr.thomas.lefebvre.toutougo.ui.presentation


import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.database.helper.DogHelper
import fr.thomas.lefebvre.toutougo.database.model.User
import fr.thomas.lefebvre.toutougo.database.helper.UserHelper
import fr.thomas.lefebvre.toutougo.database.model.Dog
import kotlinx.coroutines.*


class WelcomeViewModel() : ViewModel() {

    // -------------------- VARIABLE GENERIC ------------------------

    val currentUser = FirebaseAuth.getInstance().currentUser!!
    private val userHelper = UserHelper()
    private val dogHelper = DogHelper()
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val activity = MutableLiveData<Boolean>().apply { value = false }

    // -------------------- VARIABLE FOR USER INFOS ------------------------

    val nameUser = MutableLiveData<String>()
    val emailUser = MutableLiveData<String>()
    val ageUser = MutableLiveData<String>()
    val sexeUser = MutableLiveData<String>()
    val descriptionUser = MutableLiveData<String>()
    val photoUserUrl = MutableLiveData<String>().apply { value = "noPhoto" }


    // -------------------- VARIABLE FOR DOGS INFOS ------------------------

    val nameDog = MutableLiveData<String>()
    val heightDog = MutableLiveData<String>()
    val ageDog = MutableLiveData<String>()
    val sexeDog = MutableLiveData<String>()
    val raceDog = MutableLiveData<String>()
    val descriptionDog = MutableLiveData<String>()
    val photoDogUrl = MutableLiveData<String>().apply { value = "noPhoto" }
    val numberDog = MutableLiveData<Int>().apply { value=0 }
    val refPhoto = MutableLiveData<String>().apply { value="noPhoto" }

    // -------------------- VARIABLE FOR LIST      ------------------------

    val listInt = ArrayList<String>()
    val listSexe = ArrayList<String>()
    val listRace = ArrayList<String>()
    val listIntDog = ArrayList<String>()
    val listSexeDog = ArrayList<String>()

    // -------------------- INIT VARIABLES ------------------------

    init {
        Log.d("DEBUG", "init view model")

       getUserAuth()

        initListSpinner()

    }

    // -------------------- METHOD FOR SET LIST ------------------------



    fun initListSpinner() {
        for (i in 18..99) {//init list age
            listInt.add(i.toString())
        }
        //init list sex
        listSexe.add("Femme")
        listSexe.add("Homme")

        //init list race
        listRace.add("Boxer")
        listRace.add("Caniche")
        listRace.add("Berger Allemand")

        //init list int dog
        for (i in 0..99) {//init list age
            listIntDog.add(i.toString())
        }
        //init list sex dog
        listSexeDog.add("Mâle")
        listSexeDog.add("Femelle")
        Log.d("DEBUG", "init spinner in viewmodel")
    }

    // -------------------- METHOD FOR USER INFOS ------------------------

    //CREATE USER
    fun createUser() {
        val user = User(
            currentUser.uid,
            nameUser.value!!,
            emailUser.value!!,
            photoUserUrl.value.toString(),
            ageUser.value!!.toInt(),
            sexeUser.value!!,
            descriptionUser.value!!,
            numberDog.value!!
        )
        uiScope.launch {
            updateUserInDatabase(user)
        }
    }

    private suspend fun updateUserInDatabase(user: User) {
        withContext(Dispatchers.IO) {
            userHelper.createUser(user)
        }
    }

    //CHECK IF HAVE ALL INFORMATION
    fun checkAllInfos(): Boolean {
        return (ageUser.value != null
                && nameUser.value != null
                && emailUser.value != null
                && sexeUser.value != null
                && descriptionUser.value != null)
    }

    // -------------------- METHOD FOR GET USER ------------------------
    fun getUserAuth() {
        nameUser.value = currentUser.displayName//get name user from firebase auth
        photoUserUrl.value = currentUser.photoUrl.toString()//get photo user from firebase auth
        emailUser.value = currentUser.email//get email user from firebase auth
        Log.d("DEBUG", "activity value = " + activity.value.toString())
    }


    //GET USER
    fun getUserFirebase() {
        uiScope.launch {
            getUserInDatabase()
        }
    }

    private suspend fun getUserInDatabase() {
        withContext(Dispatchers.IO) {
            userHelper.getUserById(currentUser.uid).addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user: User? = documentSnapshot.toObject(User::class.java)
                    numberDog.value = user!!.numberDog
                    nameUser.value = user!!.name
                    emailUser.value = user.email
                    ageUser.value = user.age.toString()
                    sexeUser.value = user.sexe
                    descriptionUser.value = user.description
                    photoUserUrl.value = user.photoUrl

                    Log.d("DEBUG", "load user from firestore")
                }
            }
                .addOnCompleteListener {  }

        }
    }



    //UPDATE NUMBER DOG OF USER
    fun updateNumberDogUser() {
        uiScope.launch {
            updateNumberDogUserInDatabase()
        }
    }

    private suspend fun updateNumberDogUserInDatabase() {
        withContext(Dispatchers.IO) {
            userHelper.updateNumberDogUser(currentUser.uid, numberDog.value!! + 1)
                .addOnSuccessListener { Log.d("DEBUG", "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.d("DEBUG", "Error updating document", e) }
        }
    }


// -------------------- METHOD FOR DOGS INFOS ------------------------

    //CREATE DOG
    fun createDog() {

        val dog = Dog(
            currentUser.uid + numberDog.value.toString(),
            nameDog.value!!,
            heightDog.value!!.toInt(),
            ageDog.value!!.toInt(),
            sexeDog.value!!,
            raceDog.value!!,
            photoDogUrl.value.toString(),
            descriptionDog.value!!,
            currentUser.uid,
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

    //CHECK IF HAVE ALL INFORMATION DOG
    fun checkAllInfosDog(): Boolean {
        return (nameDog.value != null
                && ageDog.value != null
                && heightDog.value != null
                && sexeDog.value != null
                && descriptionDog.value != null
                && raceDog.value != null)
    }
}
