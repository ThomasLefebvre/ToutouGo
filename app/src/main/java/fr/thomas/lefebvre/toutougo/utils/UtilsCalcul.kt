package fr.thomas.lefebvre.toutougo.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun View.setBitmapFromView(): Bitmap {
    val bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
    val canvas=Canvas(bitmap)
    draw(canvas)
    return bitmap
}

fun computeDistance(deviceLat: Double, deviceLng: Double, placeLat: Double, placeLong: Double): Long {

    val R = 6371 // Radius of earth
    val latDistance = Math.toRadians(placeLat - deviceLat)
    val lonDistance = Math.toRadians(placeLong - deviceLng)
    val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + (Math.cos(Math.toRadians(deviceLat)) * Math.cos(
        Math.toRadians(placeLat)
    )
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2))
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    var distance = R.toDouble() * c * 1000.0 // convert to meters

    val height = 0.0 - 0.0
    distance = Math.pow(distance, 2.0) + Math.pow(height, 2.0)
    //Rounded
    return Math.round(Math.sqrt(distance))
//    return distance
}

fun hourToMillis(hours:Int):Long{
   val hoursMillis:Long= (hours*60*60*1000).toLong()
    return hoursMillis
}

fun minuteToMillis(minutes:Int):Long{
    val minuteMillis:Long= (minutes*60*1000).toLong()
    return minuteMillis
}