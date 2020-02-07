package fr.thomas.lefebvre.toutougo.database.helper


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import fr.thomas.lefebvre.toutougo.database.model.PhotoPlace
import fr.thomas.lefebvre.toutougo.database.model.User

class PhotoPlaceHelper {

    private val COLLECTION_NAME = "photoPlaces"

    // ---- COLLECTION REFERENCE ----
    private fun getPhotoPlacesCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // ---- CREATE PHOTO PLACE ----
    fun createPhotoPlace(photoPlace: PhotoPlace):Task<Void>{
        return getPhotoPlacesCollection().document(photoPlace.uidPhoto).set(photoPlace)
    }

    // ---- GET PHOTO BY ID ----
    fun getPhotoPlaceById(uid: String): Task<DocumentSnapshot> {
        return getPhotoPlacesCollection().document(uid).get()
    }

    // ---- GET ALL PHOTO FROM ID PLACE ----
    fun getAllPhotoPlace(uidPlace:String):Task<QuerySnapshot>{
        return getPhotoPlacesCollection().whereEqualTo("uidPlace",uidPlace).get()
    }

    // ---- DELETE PHOTO BY ID ----
    fun deletePhotoPlace(uid: String): Task<Void> {
        return getPhotoPlacesCollection().document(uid).delete()
    }







}