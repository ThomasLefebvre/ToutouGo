package fr.thomas.lefebvre.toutougo.database.model

data class Dog(
    val uid:String="",
    val name:String="",
    val height:Int=0,
    val age:Int=0,
    val sexe:String="",
    val race:String="",
    val photoUrl:String="",
    val description:String="",
    val uidUser:String="",
    val refPhoto:String=""
)