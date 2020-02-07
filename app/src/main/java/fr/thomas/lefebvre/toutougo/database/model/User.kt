package fr.thomas.lefebvre.toutougo.database.model

data class User (
    val uid:String="",
    val name:String="",
    val email:String="",
    val photoUrl:String="",
    val age:Int=0,
    val sexe:String="",
    val description:String="",
    val numberDog:Int=0

) {
}