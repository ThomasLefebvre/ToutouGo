package fr.thomas.lefebvre.toutougo.utils


import android.content.Context
import android.opengl.Visibility
import android.view.View
import fr.thomas.lefebvre.toutougo.R
import java.text.SimpleDateFormat
import java.util.*


fun setDateToString(date: Long): String {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    return simpleDateFormat.format(date)
}

fun setTimeToString(hour: Int, minute: Int): String {
    val sb = java.lang.StringBuilder()
    sb.append(hour.toString())
    sb.append("H")
    if (minute < 10) {
        sb.append("0$minute")
    } else {
        sb.append(minute.toString())
    }
    return sb.toString()
}

fun setDistanceToString(distance:Double):String{
    val distanceInt = distance.toInt()
    if (distanceInt < 9999) {
        return  distanceInt.toString() + "m"
    } else {
        return (distanceInt / 1000).toString() + "km"
    }
}

fun setVisibilityView(boolean: Boolean):Int{
    if(boolean){
        return  View.VISIBLE
    }
    else{
        return View.GONE
    }
}
