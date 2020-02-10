package fr.thomas.lefebvre.toutougo.database.model

data class Place (
    val uid:String="",
    val name:String="",
    val description:String="",
    val adress:String="",
    val lat:Double=0.0,
    val lng:Double=0.0,
    val distance:Long=0,
    val onLine:Boolean=false,
    val photoUrlMain:String=""

){
}