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

    // ---- CREATE EVENT ----
    fun createEvent(event:Event):Task<Void>{
        return getEventsCollection().document(event.uidEvent).set(event)
    }

    // ---- GET EVENT BY ID ----
    fun getEventById(uid: String): Task<DocumentSnapshot> {
        return getEventsCollection().document(uid).get()
    }

    // ---- DELETE EVENT BY ID ----

    fun deleteEvent(uid: String): Task<Void> {
        return getEventsCollection().document(uid).delete()
    }


    // ---- GET ALL EVENT ----
    fun getAllEvent():Task<QuerySnapshot>{
        return getEventsCollection().orderBy("dateEvent",Query.Direction.ASCENDING).get()
    }


    // ---- UPDATE EVENT ----
    fun updateNumberCustomer(uid: String,numberCustomer:Int): Task<Void> {
        return getEventsCollection().document(uid).update("numberCustomer",numberCustomer)
    }


}