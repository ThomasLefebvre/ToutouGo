package fr.thomas.lefebvre.toutougo.database.helper


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import fr.thomas.lefebvre.toutougo.database.model.Place
import fr.thomas.lefebvre.toutougo.database.model.User

class PlaceHelper {

    private val COLLECTION_NAME = "places"

    // ---- COLLECTION REFERENCE ----
    private fun getPlacesCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // ---- CREATE PLACE ----
    fun createPlace(place:Place):Task<Void>{
        return getPlacesCollection().document(place.uid).set(place)
    }

    // ---- GET PLACE BY ID ----
    fun getPlaceById(uid: String): Task<DocumentSnapshot> {
        return getPlacesCollection().document(uid).get()
    }

    // ---- DELETE PLACE BY ID ----

    fun deletePlace(uid: String): Task<Void> {
        return getPlacesCollection().document(uid).delete()
    }


    // ---- GET ALL PLACES ----
    fun getAllPlace():Task<QuerySnapshot>{
        return getPlacesCollection().whereEqualTo("onLine",true).get()
    }


    // ---- UPDATE PHOTO ----
    fun updatePhotoPlaceIcone(uid: String,photoUrlMain:String): Task<Void> {
        return getPlacesCollection().document(uid).update("photoUrlMain",photoUrlMain)
    }


}