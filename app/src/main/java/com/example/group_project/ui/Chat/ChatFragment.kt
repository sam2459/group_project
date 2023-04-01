package com.example.group_project.ui.Chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.group_project.Local_user
import com.example.group_project.R
import com.example.group_project.databinding.FragmentChatBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val users =ArrayList<friend>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mRecyclerView=binding.recyclerUserView
        // Set its Properties using LinearLayout
        mRecyclerView.setLayoutManager(LinearLayoutManager(this@ChatFragment.context))
        // Set RecyclerView Adapter
        Log.d("Yes", Local_user.name)

        val db = FirebaseDatabase.getInstance().getReference().child(Local_user.name).child("Friends")

        db.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("fire", "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                try {
                    val comment: friend? = dataSnapshot.getValue(friend::class.java) ?: return

                    users.add(comment!!)
                    val myAdapter = UserAdapter(this@ChatFragment.context!!, users)
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
                try {
                    val comment: friend? = dataSnapshot.getValue(friend::class.java) ?: return
                    if (comment != null) for(i in users.indices) {
                        if (users[i].name == comment.name) {
                            users[i].msg = comment.msg
                            users[i].num = comment.num
                        }
                        val myAdapter = UserAdapter(this@ChatFragment.context!!, users)
                        mRecyclerView.setAdapter(myAdapter)
                        Log.d("fire", comment.name + comment.msg)
                    }
                } catch (e: Exception) {
                    return
                }
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
                    this@ChatFragment.context, "Failed to load comments.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


        setHasOptionsMenu(true);
        return root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_options, menu)
        super.onCreateOptionsMenu(menu!!, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.add_friend->{
                val intent = Intent(this@ChatFragment.context, AddFriend::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_about -> {
                Toast.makeText(this@ChatFragment.context, "About Button Clicked !",
                    Toast.LENGTH_LONG).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}