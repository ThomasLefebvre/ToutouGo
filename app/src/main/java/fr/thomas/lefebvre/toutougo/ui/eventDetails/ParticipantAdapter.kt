package fr.thomas.lefebvre.toutougo.ui.eventDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.thomas.lefebvre.toutougo.database.model.Participation
import fr.thomas.lefebvre.toutougo.databinding.ListItemParticipantBinding


class ParticipantAdapter(val clickListener: ParticipantListener) : ListAdapter<Participation, ParticipantAdapter.ViewHolder>(ParticipantDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position),clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemParticipantBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Participation,clickListener: ParticipantListener) {
            binding.participant = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemParticipantBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class ParticipantDiffCallback : DiffUtil.ItemCallback<Participation>() {

    override fun areItemsTheSame(oldItem: Participation, newItem: Participation): Boolean {
        return oldItem.uidParticipation == newItem.uidParticipation
    }


    override fun areContentsTheSame(oldItem: Participation, newItem: Participation): Boolean {
        return oldItem == newItem
    }


}

class ParticipantListener(val clickListener: (participationId: Participation) -> Unit) {
    fun onClick(participation: Participation) = clickListener(participation)
}