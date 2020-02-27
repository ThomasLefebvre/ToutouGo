package fr.thomas.lefebvre.toutougo.database.helper


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import fr.thomas.lefebvre.toutougo.database.model.Event
import fr.thomas.lefebvre.toutougo.database.model.Place
import fr.thomas.lefebvre.toutougo.database.model.User

class EventHelper {

    private val COLLECTION_NAME = "events"

    // ---- COLLECTION REFERENCE ----
    private fun getEventsCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // ---- CREATE PLACE ----
    fun createEvent(event:Event):Task<Void>{
        return getEventsCollection().document(event.uidEvent).set(event)
    }

    // ---- GET PLACE BY ID ----
    fun getEventById(uid: String): Task<DocumentSnapshot> {
        return getEventsCollection().document(uid).get()
    }

    // ---- DELETE PLACE BY ID ----

    fun deleteEvent(uid: String): Task<Void> {
        return getEventsCollection().document(uid).delete()
    }


    // ---- GET ALL PLACES ----
    fun getAllEvent():Task<QuerySnapshot>{
        return getEventsCollection().orderBy("dateEvent",Query.Direction.ASCENDING).get()
    }


    // ---- UPDATE USER ----
    fun updateNumberCustomer(uid: String,numberCustomer:Int): Task<Void> {
        return getEventsCollection().document(uid).update("numberCustomer",numberCustomer)
    }


}