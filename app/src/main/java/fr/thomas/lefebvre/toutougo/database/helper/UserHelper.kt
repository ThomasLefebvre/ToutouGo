package fr.thomas.lefebvre.toutougo.database.helper


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import fr.thomas.lefebvre.toutougo.database.model.User

class UserHelper {

    private val COLLECTION_NAME = "users"

    // ---- COLLECTION REFERENCE ----
    private fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // ---- CREATE USER ----
    fun createUser(user:User):Task<Void>{
        return getUsersCollection().document(user.uid).set(user)
    }

    // ---- GET USER BY ID ----
    fun getUserById(uid: String): Task<DocumentSnapshot> {
        return getUsersCollection().document(uid).get()
    }

    // ---- DELETE USER BY ID ----

    fun deleteUser(uid: String): Task<Void> {
        return getUsersCollection().document(uid).delete()
    }

    // ---- UPDATE USER ----
    fun updateNumberDogUser(uid: String,number:Int): Task<Void> {
        return getUsersCollection().document(uid).update("numberDog",number)
    }






}