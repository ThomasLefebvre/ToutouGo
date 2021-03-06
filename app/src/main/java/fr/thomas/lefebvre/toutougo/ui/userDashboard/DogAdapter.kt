package fr.thomas.lefebvre.toutougo.ui.userDashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.databinding.ListItemDogBinding

class DogAdapter(val clickListener: DogListener,val isVisible:Boolean) : ListAdapter<Dog, DogAdapter.ViewHolder>(DogDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position),clickListener,isVisible)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemDogBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Dog,clickListener: DogListener,isVisible: Boolean) {
            binding.dog = item
            binding.clickListener = clickListener
            binding.isVisible=isVisible
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemDogBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class DogDiffCallback : DiffUtil.ItemCallback<Dog>() {

    override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
        return oldItem.uid == newItem.uid
    }


    override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
        return oldItem == newItem
    }


}

class DogListener(val clickListener: (dogId: Dog) -> Unit) {
    fun onClick(dog: Dog) = clickListener(dog)
}