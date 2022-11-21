package fr.thomas.lefebvre.toutougo.ui.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.thomas.lefebvre.toutougo.database.model.Event
import fr.thomas.lefebvre.toutougo.databinding.ListItemEventBinding


class EventAdapter(val lat:Double,val long:Double,val clickListener: EventListener) : ListAdapter<Event, EventAdapter.ViewHolder>(EventDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position),lat,long,clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemEventBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Event,lat: Double,long: Double,clickListener: EventListener) {
            binding.event = item
            binding.clickListener = clickListener
            binding.latitude=lat
            binding.longitude=long
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemEventBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

// TEST CODE


class EventDiffCallback : DiffUtil.ItemCallback<Event>() {

    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.uidEvent == newItem.uidEvent
    }


    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }


}

class EventListener(val clickListener: (eventUid: Event) -> Unit) {
    fun onClick(event: Event) = clickListener(event)
}
