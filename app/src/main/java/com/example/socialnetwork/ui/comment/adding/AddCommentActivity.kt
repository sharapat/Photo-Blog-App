package com.example.socialnetwork.ui.comment.adding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialnetwork.R
import com.example.socialnetwork.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_comment.*
import android.view.View

class AddCommentActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private var postId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)
        postId = intent.getStringExtra("PostId") ?: ""
        btnAddComment.setOnClickListener {
            if (!etComment.text.isNullOrEmpty()) {
                progressBar.visibility = View.VISIBLE
                db.collection("posts").document(postId).get().addOnSuccessListener {
                    if (it.exists()) {
                        val post = it.toObject(Post::class.java)
                        var username = ""
                        db.collection("users").document(mAuth.currentUser!!.uid).get().addOnSuccessListener { user ->
                            username = user.get("username").toString()

                            post?.comments?.add(mapOf("username" to username, "comment_text" to etComment.text.toString()))
                            val newPost = mutableMapOf<String, Any?>()
                            newPost["id"] = post?.id
                            newPost["theme"] = post?.theme
                            newPost["username"] = post?.username
                            newPost["text"] = post?.text
                            newPost["like"] = post?.like
                            newPost["dislike"] = post?.dislike
                            newPost["userId"] = post?.userId
                            newPost["comments"] = post?.comments
                            db.collection("posts").document(postId).set(newPost)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Pikirińiz qosıldı", Toast.LENGTH_LONG).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Pikirinizdi qosiwda qatelik juz berdi", Toast.LENGTH_LONG).show()
                                }
                                .addOnCompleteListener {
                                    progressBar.visibility = View.GONE
                                    finish()
                                }
                        }
                    }
                }
            }
        }
    }
}