package fr.thomas.lefebvre.toutougo.ui.detailPlace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.thomas.lefebvre.toutougo.database.model.Comment
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.database.model.PhotoPlace
import fr.thomas.lefebvre.toutougo.database.model.Place
import fr.thomas.lefebvre.toutougo.databinding.ListItemCommentBinding
import fr.thomas.lefebvre.toutougo.databinding.ListItemDogBinding
import fr.thomas.lefebvre.toutougo.databinding.ListItemPhotoPlaceBinding
import fr.thomas.lefebvre.toutougo.databinding.ListItemPlaceBinding

class CommentAdapter() : ListAdapter<Comment, CommentAdapter.ViewHolder>(CommentDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemCommentBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Comment) {
            binding.comment = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCommentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {

    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.uid == newItem.uid
    }


    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }


}

