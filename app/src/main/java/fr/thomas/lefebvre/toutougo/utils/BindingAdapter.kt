package fr.thomas.lefebvre.toutougo.utils


import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.toutougo.R
import fr.thomas.lefebvre.toutougo.database.model.PhotoPlace
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@BindingAdapter("imageUrl", "error")
fun loadImage(view: ImageView, url: String?, error: Drawable) {
    Picasso.get().load(url).error(error).into(view)
}

@BindingAdapter("idCurrentUser","idUserDog")
fun ImageButton.setVisibility(idCurrentUser:String,idUserDog:String){
    if(idCurrentUser==idUserDog){
        visibility=View.VISIBLE
    }
    else{
        visibility=View.GONE
    }
}


@BindingAdapter("participant")
fun TextView.setParticipants(participants:Int){
    val sb=java.lang.StringBuilder()
    sb.append(participants)
    sb.append(" "+context.getString(R.string.participants))
    text=sb.toString()
}

@BindingAdapter("date")
fun TextView.setDateSting(date:Long){
    if(date>0){
        val dateLong= Date(date)
        val sdf=SimpleDateFormat("dd/MM/yyyy")
        text = sdf.format(dateLong)
    }

}

@BindingAdapter("hour","minute")
fun  TextView.setTime(hour:Int?,minute:Int?){
    if(hour!=null){
        val sb=java.lang.StringBuilder()
        sb.append(hour.toString())
        sb.append(context.getString(R.string.hour))
        if (minute != null) {
            if(minute<10){
                sb.append("0$minute")
            }else{
                sb.append(minute.toString())
            }
        }
        text=sb.toString()


    }
}


@BindingAdapter("likeOrNot")
fun ImageView.setLike(likeOrNot:Boolean){
    if(likeOrNot){
        setImageDrawable(context.getDrawable(R.drawable.ic_like))
    }
    else{
        setImageDrawable(context.getDrawable(R.drawable.ic_dislike))
    }
}



@BindingAdapter("nameUser")
fun TextView.setTextPresentation(nameUser: String?) {
    val stringBuilder = StringBuilder()
    stringBuilder.append(context.getString(R.string.welcome_text))
    if (nameUser != null) {
        stringBuilder.append(nameUser)
    }

    stringBuilder.append(context.getString(R.string.welcome_text_two))
    text = stringBuilder.toString()
}

@BindingAdapter("ageUser")
fun TextView.setAge(ageUser: String?) {
    val stringBuilder = StringBuilder()
    stringBuilder.append(ageUser)
    stringBuilder.append(" ")
    stringBuilder.append(context.getString(R.string.ans))
    text = stringBuilder.toString()

}

@BindingAdapter("ageDog")
fun TextView.setAgeDog(ageUser: Int?) {
    val stringBuilder = StringBuilder()
    stringBuilder.append(ageUser.toString())
    stringBuilder.append(" ")
    stringBuilder.append(context.getString(R.string.ans))
    text = stringBuilder.toString()

}


@BindingAdapter("numberChien")
fun TextView.setNumberChienUser(numberChien: Int) {
    val stringBuilder = StringBuilder()
    stringBuilder.append(numberChien.toString())
    stringBuilder.append(" ")
    if (numberChien < 2) {
        stringBuilder.append(context.getString(R.string.chien))
    } else {
        stringBuilder.append(context.getString(R.string.chiens))
    }
    text = stringBuilder.toString()

}

@BindingAdapter("numberChienTitle")
fun TextView.setNumberChienTitleUser(numberChien: Int) {
    val stringBuilder = StringBuilder()
    if (numberChien == 1) {
        stringBuilder.append(context.getString(R.string.mon_chien))
    } else {
        stringBuilder.append(context.getString(R.string.mes_chiens))
    }
    text = stringBuilder.toString()

}

@BindingAdapter("poids")
fun TextView.setHeight(height: Int?) {
    val stringBuilder = StringBuilder()
    stringBuilder.append(height.toString())
    stringBuilder.append(" ")
    stringBuilder.append(context.getString(R.string.kg))
    text = stringBuilder.toString()

}

@BindingAdapter("activity")
fun FloatingActionButton.setVisibility(activity: Boolean) {
    if (activity) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }

}


@BindingAdapter("distance")
fun TextView.setDoubleToString(distance: Double) {
    val distanceInt = distance.toInt()
    if (distanceInt < 9999) {
        text = distanceInt.toString() + "m"
    } else {
        text = (distanceInt / 1000).toString() + "km"
    }
}








