package fr.thomas.lefebvre.toutougo.ui.userDetail


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


class UserDetailViewModel() : ViewModel() {

    // -------------------- VARIABLE GENERIC ------------------------


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val userHelper=UserHelper()
    private val dogHelper=DogHelper()

    // -------------------- VARIABLE FOR USER ------------------------

    val userDetail=MutableLiveData<User>()
    val ageUser=MutableLiveData<String>()
    val currentUserUid=FirebaseAuth.getInstance().currentUser!!.uid

    // -------------------- VARIABLE FOR DOG ------------------------

    val listDog = MutableLiveData<ArrayList<Dog>>()

    // -------------------- METHOD FOR GET USER ------------------------

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    //GET USER
    fun getUserDetail(uidUser:String) {
        uiScope.launch {
            getUserInDatabase(uidUser)
        }
    }

    private suspend fun getUserInDatabase(uidUser: String) {
        withContext(Dispatchers.IO) {
            userHelper.getUserById(uidUser).addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user: User? = documentSnapshot.toObject(User::class.java)

                    userDetail.value=user
                    ageUser.value=user!!.age.toString()

                    Log.d("DEBUG", "load user from firestore")
                }
            }
                .addOnCompleteListener {  }

        }
    }





    // -------------------- METHOD FOR GET DOG ------------------------

    fun getDogUser(uidUser: String) {
        uiScope.launch {
            getDodUserFromUserFireStore(uidUser)
        }
    }

    private suspend fun getDodUserFromUserFireStore(uidUser: String) {
        withContext(Dispatchers.IO) {
            dogHelper.getAllDogUser(uidUser).addOnSuccessListener { documents ->
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


}
