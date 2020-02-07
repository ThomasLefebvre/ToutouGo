package fr.thomas.lefebvre.toutougo.ui.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.database.model.PhotoPlace
import fr.thomas.lefebvre.toutougo.databinding.ListItemDogBinding
import fr.thomas.lefebvre.toutougo.databinding.ListItemPhotoPlaceBinding

class PhotoPlaceAdapter(val clickListener: PhotoPlaceListener) : ListAdapter<PhotoPlace, PhotoPlaceAdapter.ViewHolder>(PhotoPlaceDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position),clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemPhotoPlaceBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: PhotoPlace,clickListener: PhotoPlaceListener) {
            binding.photoPlace = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPhotoPlaceBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class PhotoPlaceDiffCallback : DiffUtil.ItemCallback<PhotoPlace>() {

    override fun areItemsTheSame(oldItem: PhotoPlace, newItem: PhotoPlace): Boolean {
        return oldItem.uidPhoto == newItem.uidPhoto
    }


    override fun areContentsTheSame(oldItem: PhotoPlace, newItem: PhotoPlace): Boolean {
        return oldItem == newItem
    }


}

class PhotoPlaceListener(val clickListener: (photoPlaceId: PhotoPlace) -> Unit) {
    fun onClick(photoPlace: PhotoPlace) = clickListener(photoPlace)
}