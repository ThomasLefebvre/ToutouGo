package fr.thomas.lefebvre.toutougo.database.model

data class Event(

    val uidEvent: String = "",
    val nameEvent: String = "",
    val descriptionEvent: String = "",
    val dateEvent: Long = 0,
    val hourEvent: Int = 0,
    val minuteEvent: Int = 0,
    val numberCustomer:Int=0,
    val maxCustomer: Int = 0,
    val namePlaceEvent: String = "",
    val latPlace:Double=0.0,
    val lngPlace:Double=0.0,
    val distance:Long=0,
    val uidPlaceEvent: String = "",
    val uidCreator: String = "",
    val photoEvent:String=""
)