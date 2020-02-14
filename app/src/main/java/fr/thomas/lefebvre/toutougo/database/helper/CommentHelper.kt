package fr.thomas.lefebvre.toutougo.database.helper


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import fr.thomas.lefebvre.toutougo.database.model.Comment
import fr.thomas.lefebvre.toutougo.database.model.Place
import fr.thomas.lefebvre.toutougo.database.model.User

class CommentHelper {

    private val COLLECTION_NAME = "comments"

    // ---- COLLECTION REFERENCE ----
    private fun getCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // ---- CREATE COMMENT ----
    fun createComment(comment: Comment):Task<Void>{
        return getCollection().document(comment.uid).set(comment)
    }

    // ---- GET COMMENT BY ID ----
    fun getCommentById(uid: String): Task<DocumentSnapshot> {
        return getCollection().document(uid).get()
    }


    // ---- GET ALL COMMENT ----
    fun getAllComment(uidComment:String):Task<QuerySnapshot>{
        return getCollection().whereEqualTo("uid",uidComment).get()
    }




}