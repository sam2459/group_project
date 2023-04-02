package com.example.group_project.ui.Chat

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.group_project.Local_user
import com.example.group_project.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue


class Chat : AppCompatActivity() {
    lateinit var layout: LinearLayout
    lateinit var sendButton: ImageView
    lateinit var messageArea: EditText
    lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        layout = findViewById<View>(R.id.layout1) as LinearLayout
        sendButton = findViewById<View>(R.id.sendButton) as ImageView
        messageArea = findViewById<View>(R.id.messageArea) as EditText
        scrollView = findViewById<View>(R.id.scrollView) as ScrollView
        val intent = intent
        val target_Name = intent.getStringExtra("tName")
        val Chat_history = FirebaseDatabase.getInstance().getReference().child("Chat_history")
        FirebaseDatabase.getInstance().getReference().child(Local_user.name)
            .child("Friends").child(target_Name!!).get().addOnSuccessListener {
            Thread {
                try {
                    var x = it.getValue(friend::class.java)!!
                    Log.d("SEND_log", "${x!!.name}")
                    x.num = 0
                    FirebaseDatabase.getInstance().getReference().child(Local_user.name)
                        .child("Friends").child(target_Name).setValue(x)
                }
                catch (e:Exception){
                    return@Thread
                }
            }.start()
        }
        sendButton.setOnClickListener {

                val messageText = messageArea.text.toString()
                Log.d("send", "$messageText to $target_Name")
                if (messageText != "") {
                    var msg=ChatItem()
                    msg.from= Local_user.name
                    msg.to=target_Name
                    msg.text=messageText
                    Chat_history.push().setValue(msg)

                    FirebaseDatabase.getInstance().getReference().child(target_Name!!).child("Friends").child(Local_user.name).get().addOnSuccessListener {
                        Thread {
                            try {
                                var x = it.getValue(friend::class.java)!!
                                Log.d("SEND_log", "${x!!.name}")
                                x.num = x.num?.plus(1)
                                x.msg = messageText
                                FirebaseDatabase.getInstance().getReference().child(target_Name)
                                    .child("Friends").child(Local_user.name).setValue(x)
                                x.num = 0
                                x.name = target_Name
                                FirebaseDatabase.getInstance().getReference().child(Local_user.name)
                                    .child("Friends").child(target_Name).setValue(x)
                            }
                            catch (e:Exception){
                                return@Thread
                            }
                        }.start()
                    }

                }
                messageArea.setText("")
        }
        Chat_history.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                try {
                    val msg = dataSnapshot.getValue(ChatItem::class.java) ?: return
                    if (msg.from == Local_user.name && msg.to==target_Name) {
                        addMessageBox("You:-\n" + msg.text, 1);
                    } else if(msg.from ==target_Name && msg.to== Local_user.name){
                        addMessageBox(msg.from + ":-\n" + msg.text, 2);
                    }
                }
                catch (e:Exception){
                    return
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun addMessageBox(message: String?, type: Int) {
        val textView = TextView(this@Chat)
        textView.text = message
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(0, 0, 0, 10)
        if (type == 1) {
            lp.gravity=5
            textView.setBackgroundResource(R.drawable.rounded_corner1)
        } else {
            lp.gravity=3
            textView.setBackgroundResource(R.drawable.rounded_corner2)
        }
        textView.layoutParams = lp
        layout!!.addView(textView)
        scrollView!!.fullScroll(View.FOCUS_DOWN)
    }
}