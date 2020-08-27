package com.example.socialnetwork.ui.comment

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.socialnetwork.R
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentListAdapter : RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>() {

    var models: List<String> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populateModel(comment: String) {
            itemView.tvComment.text = comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int = models.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}