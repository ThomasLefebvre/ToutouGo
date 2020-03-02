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

    // -------------------- VARIABLE FOR USER ------------------------

    val userDetail=MutableLiveData<User>()
    val ageUser=MutableLiveData<String>()

    // -------------------- METHOD FOR GET USER ------------------------



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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }





}
