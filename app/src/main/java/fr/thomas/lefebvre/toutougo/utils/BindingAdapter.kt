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


@BindingAdapter("imageUrl", "error")
fun loadImage(view: ImageView, url: String?, error: Drawable) {
    Picasso.get().load(url).error(error).into(view)
}

@BindingAdapter("listPhotoPlace", "error","index")
fun loadImageFromListPhotoPlace(view: ImageView, listPhotoPlace: ArrayList<PhotoPlace>?, error: Drawable,index:Int) {
    if (listPhotoPlace!=null){
        Picasso.get().load(listPhotoPlace[index].photoUrl).error(error).into(view)
    }

}

@BindingAdapter("listPhotoPlace","index")
fun setVisibilityButtonNext(view: ImageButton, listPhotoPlace: ArrayList<PhotoPlace>?, index:Int) {
    if (listPhotoPlace!=null){
        if(listPhotoPlace.size-1==index){
            view.visibility=View.GONE
        }
        else{
            view.visibility=View.VISIBLE
        }
    }
    else{
        view.visibility=View.GONE
    }

}


@BindingAdapter("listPhotoPlacePrevious","indexPrevious")
fun setVisibilityButtonPrevious(view: ImageButton, listPhotoPlacePrev: ArrayList<PhotoPlace>?, indexPrev:Int) {
    if (listPhotoPlacePrev!=null){
        if(indexPrev>0){
            view.visibility=View.VISIBLE
        }
        else{
            view.visibility=View.GONE
        }
    }
    else{
        view.visibility=View.GONE
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








