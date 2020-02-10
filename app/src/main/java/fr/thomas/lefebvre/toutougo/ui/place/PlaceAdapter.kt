package fr.thomas.lefebvre.toutougo.ui.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.database.model.PhotoPlace
import fr.thomas.lefebvre.toutougo.database.model.Place
import fr.thomas.lefebvre.toutougo.databinding.ListItemDogBinding
import fr.thomas.lefebvre.toutougo.databinding.ListItemPhotoPlaceBinding
import fr.thomas.lefebvre.toutougo.databinding.ListItemPlaceBinding

class PlaceAdapter(val lat:Double,val long:Double,val clickListener: PlaceListener) : ListAdapter<Place, PlaceAdapter.ViewHolder>(PlaceDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position),lat,long,clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemPlaceBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Place,lat: Double,long: Double,clickListener: PlaceListener) {
            binding.place = item
            binding.clickListener = clickListener
            binding.latitude=lat
            binding.longitude=long
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPlaceBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {

    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.uid == newItem.uid
    }


    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }


}

class PlaceListener(val clickListener: (placeId: Place) -> Unit) {
    fun onClick(place: Place) = clickListener(place)
}