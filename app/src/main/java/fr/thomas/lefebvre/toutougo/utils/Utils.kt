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