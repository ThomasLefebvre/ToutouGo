package fr.thomas.lefebvre.toutougo.ui.place

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.thomas.lefebvre.toutougo.database.helper.*
import fr.thomas.lefebvre.toutougo.database.model.*
import fr.thomas.lefebvre.toutougo.utils.computeDistance
import fr.thomas.lefebvre.toutougo.utils.hourToMillis
import fr.thomas.lefebvre.toutougo.utils.minuteToMillis
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

    //------------- VARIABLE FOR CREATE EVENT ---------------

    val uidEvent = MutableLiveData<String>()
    val nameEvent = MutableLiveData<String>()
    val descriptionEvent = MutableLiveData<String>()
    val placeEvent = MutableLiveData<Place>()
    val dateEvent = MutableLiveData<Long>()
    val hourEvent = MutableLiveData<Int>()
    val minuteEvent = MutableLiveData<Int>()
    val maxCustomer = MutableLiveData<String>()
    val uidCreator =
        MutableLiveData<String>().apply { value = FirebaseAuth.getInstance().currentUser!!.uid }
    val namePlaceEvent = MutableLiveData<String>()

    val listInt = ArrayList<String>()
    val eventHelper = EventHelper()


    //------------- VARIABLE FOR LIST EVENT ---------------

    val listEvent = MutableLiveData<ArrayList<Event>>()

    //------------- VARIABLE FOR DETAILS EVENT ---------------
    val detailEvent = MutableLiveData<Event>()
    val userCreatorPhotoUrl=MutableLiveData<String>()
    val userHelper=UserHelper()

    //------------- VARIABLE FOR DETAILS USER ---------------

    val detailUser=MutableLiveData<String>()

    //------------- VARIABLE KNOW IS CREATE PLACE OR EVENT ---------------

    val isPlaceOrEvent = MutableLiveData<String>()
    val messageMapFragment = MutableLiveData<String>()

    //------------- VARIABLE FOR LIST PLACE ---------------
    private val placeHelper = PlaceHelper()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    val listPlace = MutableLiveData<ArrayList<Place>>()


    //------------- VARIABLE FOR DETAILS PLACE ---------------
    val detailPlace = MutableLiveData<Place>()
    val listPhotoPlace = MutableLiveData<ArrayList<PhotoPlace>>()
    val photoPlaceHelper = PhotoPlaceHelper()

    //------------- VARIABLE FOR COMMENT ---------------

    private val commentHelper = CommentHelper()
    val uidComment = MutableLiveData<String>()
    val descriptionComment = MutableLiveData<String>()

    val listComment = MutableLiveData<ArrayList<Comment>>()




    // -------------------- GET USER FOR EVENT ------------------------

    fun getUserCreator(){
        uiScope.launch {
            getUserCreatorFromDatabase()
        }
    }
    private suspend fun getUserCreatorFromDatabase(){
        withContext(Dispatchers.IO){
            userHelper.getUserById(detailEvent.value!!.uidCreator).addOnSuccessListener {  documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user: User? = documentSnapshot.toObject(User::class.java)
                    userCreatorPhotoUrl.value=user!!.photoUrl

                    Log.d("DEBUG", "load user creator from firestore ${userCreatorPhotoUrl.value}")
                } }
        }
    }


    // -------------------- GET EVENT FROM FIRESTORE FOR RECYCLER VIEW  ------------------------
    fun getEvent(userLat: Double, userLng: Double) {
        uiScope.launch {
            getEventFromFireStore(userLat, userLng)


        }
    }

    private suspend fun getEventFromFireStore(userLat: Double, userLng: Double) {
        withContext(Dispatchers.IO) {
            eventHelper.getAllEvent().addOnSuccessListener { documents ->
                if (documents.documents.isEmpty()) {
                    Log.d("EVENT", "No event")
                    listEvent.value=null
                } else {
                    val list = ArrayList<Event>()
                    for (document in documents) {

                        var event = document.toObject(Event::class.java)
                        event = Event(
                            event.uidEvent,
                            event.nameEvent,
                            event.descriptionEvent,
                            event.dateEvent,
                            event.hourEvent,
                            event.minuteEvent,
                            event.numberCustomer,
                            event.maxCustomer,
                            event.namePlaceEvent,
                            event.latPlace,
                            event.lngPlace,
                            computeDistance(userLat, userLng, event.latPlace, event.lngPlace),
                            event.uidPlaceEvent,
                            event.uidCreator,
                            event.photoEvent
                        )
                        if((event.dateEvent+ hourToMillis(event.hourEvent)+ minuteToMillis(event.minuteEvent))>System.currentTimeMillis()){//check if event is actually
                            Log.d("TIME",event.nameEvent)
                            Log.d("TIME",("date event: " +(event.dateEvent+ hourToMillis(event.hourEvent)+ minuteToMillis(event.minuteEvent)).toString()))
                            Log.d("TIME",System.currentTimeMillis().toString())
                            list.add(event)
                        }



                    }
                    list.sortWith(Comparator<Event> { p1, p2 ->
                        when {
                            p1!!.distance > p2!!.distance -> 1
                            p1.distance == p2.distance -> 0
                            else -> -1
                        }
                    })

                    listEvent.value = list
                    Log.d("EVENT", "YES event")
                }

            }
        }
    }


    // -------------------- CREATE EVENT  ------------------------
    fun createEvent() {
        uidEvent.value = placeEvent.value!!.uid + System.currentTimeMillis().toString()

        val event = Event(
            uidEvent.value!!,
            nameEvent.value!!,
            descriptionEvent.value!!,
            dateEvent.value!!,
            hourEvent.value!!,
            minuteEvent.value!!,
            1,
            maxCustomer.value!!.toInt(),
            namePlaceEvent.value!!,
            placeEvent.value!!.lat,
            placeEvent.value!!.lng,
            0,
            placeEvent.value!!.uid,
            uidCreator.value!!,
            placeEvent.value!!.photoUrlMain

            )
        uiScope.launch {
            createEventInDatabase(event)
        }
    }


    private suspend fun createEventInDatabase(event: Event) {
        withContext(Dispatchers.IO) {
            eventHelper.createEvent(event)
        }
    }


    fun checkAllInfosEvent(): Boolean {
        return (placeEvent.value != null
                &&dateEvent.value!=null
                &&hourEvent.value!=null
                &&nameEvent.value!=null
                &&descriptionEvent.value!=null
                &&maxCustomer.value!=null
                )
    }

    fun clearInfosEvent() {

        nameEvent.value = null
        descriptionEvent.value = null
        placeEvent.value = null
        namePlaceEvent.value=null
        dateEvent.value = null
        hourEvent.value = null
        minuteEvent.value = null
        maxCustomer.value = null
    }



    // -------------------- METHOD FOR SET LIST ------------------------

    init {
        initListSpinner()
    }


    fun initListSpinner() {
        for (i in 2..10) {//init list age
            listInt.add(i.toString())
        }


    }

    // -------------------- CREATE COMMENT  ------------------------
    fun createComment(likeOrNot: Boolean) {
        uidComment.value = detailPlace.value!!.uid + System.currentTimeMillis().toString()

        val comment = Comment(
            uidComment.value!!,
            System.currentTimeMillis(),
            descriptionComment.value!!,
            likeOrNot,
            currentUser!!.displayName!!,
            detailPlace.value!!.uid,
            currentUser.uid

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

    fun deleteComment(uidComment:String){
        uiScope.launch {
            deleteCommentDataBase(uidComment)
        }
    }

    private suspend fun deleteCommentDataBase(uidComment: String) {
        withContext(Dispatchers.IO){
            commentHelper.deleteComment(uidComment)
        }
    }


    fun checkAllInfosComment(): Boolean {
        return (descriptionComment.value != null
                )
    }

    // -------------------- GET COMMENT FROM FIRESTORE FOR RECYCLER VIEW  ------------------------
    fun getComment() {
        listComment.value = null
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
                        Log.d("DEBUG", "YES photos")
                    }

                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

