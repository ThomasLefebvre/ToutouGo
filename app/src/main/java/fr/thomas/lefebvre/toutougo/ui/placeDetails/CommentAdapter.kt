package fr.thomas.lefebvre.toutougo.ui.placeDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.thomas.lefebvre.toutougo.database.model.Comment
import fr.thomas.lefebvre.toutougo.database.model.Dog
import fr.thomas.lefebvre.toutougo.databinding.ListItemCommentBinding
import fr.thomas.lefebvre.toutougo.ui.userDashboard.DogListener

class CommentAdapter(val clickListener: CommentListener, val idCurrentUser:String): ListAdapter<Comment, CommentAdapter.ViewHolder>(CommentDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(getItem(position),clickListener,idCurrentUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemCommentBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Comment,clickListener: CommentListener,idCurrentUser: String) {
            binding.comment = item
            binding.clickListener=clickListener
            binding.idCurrentUser=idCurrentUser
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


class CommentListener(val clickListener: (commentId: Comment) -> Unit) {
    fun onClick(comment: Comment) = clickListener(comment)
}

