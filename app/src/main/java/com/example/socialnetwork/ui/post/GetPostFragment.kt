package com.example.socialnetwork.ui.post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.socialnetwork.R
import com.example.socialnetwork.data.Post
import com.example.socialnetwork.ui.comment.CommentActivity
import com.example.socialnetwork.ui.comment.adding.AddCommentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_get_post.*

class GetPostFragment : Fragment(R.layout.fragment_get_post) {

    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val adapter = PostAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPost.adapter = adapter
        adapter.setOnItemClickListener {
            val intent = Intent(requireContext(), CommentActivity::class.java)
            intent.putExtra("postId", it.id)
            startActivity(intent)
        }
        adapter.setOnCommentClickListener {
            val intent = Intent(requireContext(), AddCommentActivity::class.java)
            intent.putExtra("PostId", it.id)
            startActivity(intent)
        }
        adapter.setOnLike {
            db.collection("users").document(mAuth.currentUser!!.uid).get()
                .addOnSuccessListener { doc->
                    val likes : MutableMap<String, Boolean> = doc.get("likes") as MutableMap<String, Boolean>
                    if (likes.containsKey(it.id)) {
                        if (likes[it.id] == false) {
                            likes[it.id] = true
                            db.collection("users").document(mAuth.currentUser!!.uid).update("likes", likes)
                            postLiked(it)
                        } else {
                            likes[it.id] = false
                            db.collection("users").document(mAuth.currentUser!!.uid).update("likes", likes)
                            decreasePostLiked(it)
                        }
                    } else {
                        likes[it.id] = true
                        db.collection("users").document(mAuth.currentUser!!.uid).update("likes", likes)
                        postLiked(it)
                    }
                }
        }
        rvPost.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        getAllPost()
    }

    fun postLiked(post: Post) {
        db.collection("posts").document(post.id).get()
            .addOnSuccessListener {
                val likeCount = it.get("like") as Long
                db.collection("posts").document(post.id).update("like", likeCount + 1)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Siz bul postqa like bastıńız", Toast.LENGTH_LONG).show()
                    }
            }

    }

    fun decreasePostLiked(post: Post) {
        db.collection("posts").document(post.id).get()
            .addOnSuccessListener {
                val likeCount = it.get("like") as Long
                db.collection("posts").document(post.id).update("like", likeCount - 1)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Siz bul postqa like bastıńız", Toast.LENGTH_LONG).show()
                    }
            }
    }

    private fun getAllPost() {
        val result: MutableList<Post> = mutableListOf()
        db.collection("posts").addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.message.toString(), Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            result.clear()
            db.collection("posts").get().addOnSuccessListener {
                it.documents.forEach { doc ->
                    val model = doc.toObject(Post::class.java)
                    model?.id = doc.id
                    model?.let {
                        result.add(model)
                    }
                }
                adapter.models = result
                Log.d("magluwmat", result.toString())
            }
        }
    }
}