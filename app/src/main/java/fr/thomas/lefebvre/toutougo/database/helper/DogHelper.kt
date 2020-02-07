package fr.thomas.lefebvre.toutougo.database.helper

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.database.model.User

class DogHelper {

    private val COLLECTION_NAME = "dogs"

    // ---- COLLECTION REFERENCE ----
    private fun getDogsCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // ---- CREATE DOG ----
    fun createDog(dog: Dog): Task<Void> {
        return getDogsCollection().document(dog.uid).set(dog)
    }

    // ---- GET DOG BY ID ----
    fun getDogById(uid: String): Task<DocumentSnapshot> {
        return getDogsCollection().document(uid).get()
    }

    // ---- GET ALL DOG FROM ID USER ----
    fun getAllDogUser(uidUser:String):Task<QuerySnapshot>{
        return getDogsCollection().whereEqualTo("uidUser",uidUser).get()
    }

    // ---- DELETE DOG BY ID ----

    fun deleteDog(uid: String): Task<Void> {
        return getDogsCollection().document(uid).delete()
    }

    // ---- UPDATE DOG ----

    fun updatePhotoDog(uid: String,photoUrl:String): Task<Void> {
        return getDogsCollection().document(uid).update("photoUrl",photoUrl)
    }

}