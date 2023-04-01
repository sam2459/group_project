package com.example.group_project.ui.Chat


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.group_project.Local_user
import com.example.group_project.R
import com.example.group_project.databinding.FragmentChatBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val users = ArrayList<friend>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mRecyclerView = binding.recyclerUserView
        // Set its Properties using LinearLayout
        mRecyclerView.setLayoutManager(LinearLayoutManager(this@ChatFragment.context))
        // Set RecyclerView Adapter
        Log.d("Yes", Local_user.name)

        val db =
            FirebaseDatabase.getInstance().getReference().child(Local_user.name).child("Friends")

        db.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("fire", "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                try {
                    val comment: friend? = dataSnapshot.getValue(friend::class.java) ?: return
                    users.add(comment!!)
                    val myAdapter = UserAdapter(this@ChatFragment.context!!, users)
                    mRecyclerView.setAdapter(myAdapter)
                    Log.d("fire", users.size.toString() + comment.name + comment.msg)
                } catch (e: Exception) {
                    return
                }
            }

            override fun onChildChanged(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {
                try {

                    val CHANNEL_ID = "new_notice"
                    var notification =
                        NotificationCompat.Builder(this@ChatFragment.context!!, CHANNEL_ID)
                            .setContentText("hi").build()

                    val comment: friend? = dataSnapshot.getValue(friend::class.java) ?: return
                    if (comment != null) for (i in users.indices) {
                        if (users[i].name == comment.name) {
                            users[i].msg = comment.msg
                            users[i].num = comment.num
                        }
                        val myAdapter = UserAdapter(this@ChatFragment.context!!, users)
                        mRecyclerView.setAdapter(myAdapter)
                        Log.d(
                            "fire",
                            "onChildChanged " + comment.name + comment.msg + comment.num.toString()
                        )
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
            R.id.add_friend -> {
                val intent = Intent(this@ChatFragment.context, AddFriend::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_about -> {
                //Toast.makeText(this@ChatFragment.context, "About Button Clicked !", Toast.LENGTH_LONG).show()
                val CHANNEL_ID = "new_notice"
                val manager = context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                val fullScreenIntent = Intent(this@ChatFragment.context, ImportantActivity::class.java)
                val fullScreenPendingIntent = PendingIntent.getActivity(this@ChatFragment.context, 0,
                    fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                var builder = NotificationCompat.Builder(this@ChatFragment.context!!, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_user)
                    .setContentTitle("sam")
                    .setContentText("Hi!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                manager.notify(1,builder.build())
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