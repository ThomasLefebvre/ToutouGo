package fr.thomas.lefebvre.toutougo.database.helper


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import fr.thomas.lefebvre.toutougo.database.model.Participation

class ParticipationHelper {

    private val COLLECTION_NAME = "participations"

    // ---- COLLECTION REFERENCE ----
    private fun getParticipationCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // ---- CREATE PARTICIPATION ----
    fun createParticipation(participation:Participation):Task<Void>{
        return getParticipationCollection().document(participation.uidParticipation).set(participation)
    }


    // ---- DELETE PARTICIPATION BY ID ----
    fun deleteParticipation(uid: String): Task<Void> {
        return getParticipationCollection().document(uid).delete()
    }


    // ---- GET ALL PARTICIPATION BY EVENT ----
    fun getAllParticipationByIdEvent(uidEvent: String):Task<QuerySnapshot>{
        return getParticipationCollection().whereEqualTo("uidEvent",uidEvent).get()
    }

    // ---- GET PARTICIPATION BY USER ----
      fun getParticipationById(uid: String): Task<DocumentSnapshot> {
        return getParticipationCollection().document(uid).get()
    }




}