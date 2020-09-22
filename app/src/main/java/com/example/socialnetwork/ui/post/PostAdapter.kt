package com.example.socialnetwork.ui.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.data.Post
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    var models: List<Post> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClicked : (model: Post) -> Unit = {}

    fun setOnItemClickListener(onItemClicked: (model: Post) -> Unit) {
        this.onItemClicked = onItemClicked
    }

    private var onCommentClicked: (model: Post) -> Unit = {}

    fun setOnCommentClickListener(onCommentClicked: (model: Post) -> Unit) {
        this.onCommentClicked = onCommentClicked
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(model: Post) {
            itemView.tvTheme.text = model.theme
            itemView.tvUsername.text = model.username
            itemView.tvPostText.text = model.text
            itemView.setOnClickListener {
                onItemClicked.invoke(model)
            }
            itemView.ivComment.setOnClickListener {
                onCommentClicked.invoke(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int = models.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}