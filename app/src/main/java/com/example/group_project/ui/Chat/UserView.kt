package com.example.group_project.ui.Chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.group_project.Local_user
import com.example.group_project.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class UserView : AppCompatActivity() {
    val users =ArrayList<friend>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_view)
        val mRecyclerView = findViewById<RecyclerView>(R.id.recyclerUserView)
        // Set its Properties using LinearLayout
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        // Set RecyclerView Adapter
        Log.d("Yes", Local_user.name)

        val db = FirebaseDatabase.getInstance().getReference().child(Local_user.name).child("Friends")

        db.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("fire", "onChildAdded:" + dataSnapshot.key!!)

                    // A new comment has been added, add it to the displayed list
                    try {
                        var comment: friend? = dataSnapshot.getValue(friend::class.java) ?: return

                        users.add(comment!!)
                        val myAdapter = UserAdapter(this@UserView, users)
                        mRecyclerView.setAdapter(myAdapter)
                        Log.d("fire", comment.name + comment.msg)
                    } catch (e: Exception) {
                        return
                    }
                }

                override fun onChildChanged(
                    dataSnapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    Log.d("fire", "onChildChanged: ${dataSnapshot.key}")

                    // ...
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    Log.d("fire", "onChildRemoved:" + dataSnapshot.key!!)

                    // ...
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("fire", "onChildMoved:" + dataSnapshot.key!!)

                    // ...
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        baseContext, "Failed to load comments.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


    }
    // Add Models to arraylist
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.add_friend->{
                val intent = Intent(this@UserView, AddFriend::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_about -> {
                Toast.makeText(applicationContext, "About Button Clicked !",
                    Toast.LENGTH_LONG).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}