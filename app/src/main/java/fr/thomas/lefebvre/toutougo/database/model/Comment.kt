package fr.thomas.lefebvre.toutougo.database.model

data class Comment(
    val uid:String="",
    val date:Long=0,
    val body:String="",
    val likeOrNot:Boolean?=null,
    val nameUser:String="",
    val uidPlace:String=""
)