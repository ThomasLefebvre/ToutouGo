package fr.thomas.lefebvre.toutougo.ui.place

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.thomas.lefebvre.toutougo.database.helper.CommentHelper
import fr.thomas.lefebvre.toutougo.database.helper.PhotoPlaceHelper
import fr.thomas.lefebvre.toutougo.database.helper.PlaceHelper
import fr.thomas.lefebvre.toutougo.database.model.Comment
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.database.model.PhotoPlace
import fr.thomas.lefebvre.toutougo.database.model.Place
import fr.thomas.lefebvre.toutougo.utils.computeDistance
import kotlinx.coroutines.*

class PlaceViewModel : ViewModel() {


    //------------- VARIABLE FOR COROUTINES ------------------

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    //------------- VARIABLE FOR CREATE PLACE ---------------

    val uidPlace = MutableLiveData<String>()
    val namePlace = MutableLiveData<String>()
    val descriptionPlace = MutableLiveData<String>()
    val latPlace = MutableLiveData<Double>()
    val lngPlace = MutableLiveData<Double>()
    val adressPlace = MutableLiveData<String>().apply { value = "" }

    //------------- VARIABLE FOR LIST PLACE ---------------
    private val placeHelper = PlaceHelper()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    val listPlace = MutableLiveData<ArrayList<Place>>()


    //------------- VARIABLE FOR DETAILS PLACE ---------------
    val detailPlace = MutableLiveData<Place>()
    val listPhotoPlace = MutableLiveData<ArrayList<PhotoPlace>>()
    val indexPhoto = MutableLiveData<Int>()
    val photoPlaceHelper = PhotoPlaceHelper()

    //------------- VARIABLE FOR COMMENT ---------------

    private val commentHelper = CommentHelper()
    val uidComment = MutableLiveData<String>()
    val descriptionComment = MutableLiveData<String>()

    val listComment = MutableLiveData<ArrayList<Comment>>()



    // -------------------- CREATE COMMENT  ------------------------
    fun createComment(likeOrNot: Boolean) {
        uidComment.value = detailPlace.value!!.uid + System.currentTimeMillis().toString()

        val comment = Comment(
            uidComment.value!!,
            System.currentTimeMillis(),
            descriptionComment.value!!,
            likeOrNot,
            currentUser!!.displayName!!,
            detailPlace.value!!.uid

        )
        uiScope.launch {
            createCommentInDatabase(comment)
        }
    }


    private suspend fun createCommentInDatabase(comment: Comment) {
        withContext(Dispatchers.IO) {
            commentHelper.createComment(comment)
        }
    }


    fun checkAllInfosComment(): Boolean {
        return (descriptionComment.value != null
                )
    }

    // -------------------- GET COMMENT FROM FIRESTORE FOR RECYCLER VIEW  ------------------------
    fun getComment() {
        listComment.value=null
        uiScope.launch {
            getCommentFromFireStore()

        }
    }

    private suspend fun getCommentFromFireStore() {
        withContext(Dispatchers.IO) {

            commentHelper.getAllComment(detailPlace.value!!.uid)
                .addOnSuccessListener { documents ->
                    if (documents.documents.isEmpty()) {
                        Log.d("DEBUG", "No comment")
                    } else {
                        val list = ArrayList<Comment>()
                        for (document in documents) {

                            var comment = document.toObject(Comment::class.java)
                            list.add(comment)

                        }

                        listComment.value = list
                        Log.d("DEBUG", "YES comment")
                    }

                }
        }
    }


    // -------------------- CREATE PLACE  ------------------------
    fun createPlace() {
        if (uidPlace.value == null) {
            uidPlace.value = currentUser!!.uid + System.currentTimeMillis().toString()
        }
        val place = Place(
            uidPlace.value!!,
            namePlace.value!!,
            descriptionPlace.value!!,
            adressPlace.value!!,
            latPlace.value!!,
            lngPlace.value!!


        )
        uiScope.launch {
            createDogInDatabase(place)
        }
    }


    private suspend fun createDogInDatabase(place: Place) {
        withContext(Dispatchers.IO) {
            placeHelper.createPlace(place)
        }
    }


    fun checkAllInfosPlace(): Boolean {
        return (namePlace.value != null
                && descriptionPlace.value != null
                && latPlace.value != null
                && lngPlace.value != null
                )
    }

    // -------------------- CLEAR PLACE AFTER SAVE  ------------------------

    fun clearPlaceAfterSave() {
        uidPlace.value = null
        namePlace.value = null
        descriptionPlace.value = null
        adressPlace.value = null
        latPlace.value = null
        lngPlace.value = null
    }

    // -------------------- GET PLACE FROM FIRESTORE FOR RECYCLER VIEW  ------------------------
    fun getPlace(userLat: Double, userLng: Double) {
        uiScope.launch {
            getPlaceFromFireStore(userLat, userLng)


        }
    }

    private suspend fun getPlaceFromFireStore(userLat: Double, userLng: Double) {
        withContext(Dispatchers.IO) {
            placeHelper.getAllPlace().addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    Log.d("DEBUG", "No places")
                } else {
                    val list = ArrayList<Place>()
                    for (document in documents) {

                        var place = document.toObject(Place::class.java)
                        place = Place(
                            place.uid,
                            place.name,
                            place.description,
                            place.adress,
                            place.lat,
                            place.lng,
                            computeDistance(userLat, userLng, place.lat, place.lng),
                            place.onLine,
                            place.photoUrlMain
                        )

                        list.add(place)

                    }
                    list.sortWith(Comparator<Place> { p1, p2 ->
                        when {
                            p1!!.distance > p2!!.distance -> 1
                            p1.distance == p2.distance -> 0
                            else -> -1
                        }
                    })

                    listPlace.value = list
                    Log.d("DEBUG", "YES places")
                }

            }
        }
    }



    // -------------------- CLICK ON DETAIL PLACE  ------------------------

    fun clickDetailPlace(place: Place) {
        detailPlace.value = place

    }

    // -------------------- GET PHOTO PLACE FROM FIRESTORE FOR RECYCLER VIEW  ------------------------
    fun getPhoto() {
        uiScope.launch {
            getPhotoPlaceFromFireStore()
            Log.d("DEBUG", uidPlace.value.toString())

        }
    }

    private suspend fun getPhotoPlaceFromFireStore() {
        withContext(Dispatchers.IO) {
            photoPlaceHelper.getAllPhotoPlace(detailPlace.value!!.uid)
                .addOnSuccessListener { documents ->
                    if (documents.documents.isEmpty()) {
                        listPhotoPlace.value = null
                        Log.d("DEBUG", "No photos")
                    } else {
                        val list = ArrayList<PhotoPlace>()
                        for (document in documents) {
                            val photPlace = document.toObject(PhotoPlace::class.java)
                            list.add(photPlace)

                        }
                        listPhotoPlace.value = list
                        indexPhoto.value = 0
                        Log.d("DEBUG", "YES photos")
                    }

                }
        }
    }
}

