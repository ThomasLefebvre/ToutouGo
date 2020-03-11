package fr.thomas.lefebvre.toutougo.utils


import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.toutougo.R
import java.text.SimpleDateFormat
import java.util.*



@BindingAdapter("imageUrl", "error")
fun loadImage(view: ImageView, url: String?, error: Drawable) {
    Picasso.get().load(url).error(error).into(view)
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
        text = setDateToString(date)
    }

}

@BindingAdapter("hour","minute")
fun  TextView.setTime(hour:Int?,minute:Int?){
if(hour!=null){
    text= setTimeToString(hour!!,minute!!,context)
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



@BindingAdapter("distance")
fun TextView.setDoubleToString(distance: Double) {
   text= setDistanceToString(distance)
}

@BindingAdapter("activity")
fun setVisibility(view: View,activity: Boolean) {
    view.visibility = setVisibilityView(activity)
}


@BindingAdapter("idCurrentUser","idUserCompare")
fun setVisibility(view: View,idCurrentUser:String,idUserCompare:String){
    val boolean=idCurrentUser==idUserCompare
    view.visibility= setVisibilityView(boolean)
}

@BindingAdapter("idCurrentUser","idCreatorUser")
fun MaterialButton.setVisibilityButton(idCurrentUser:String,idCreatorUser:String?){
    val boolean=idCurrentUser!=idCreatorUser
    visibility= setVisibilityView(boolean)

}










