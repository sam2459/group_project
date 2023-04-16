package com.example.group_project.ui.Chat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.group_project.Local_user
import com.example.group_project.R
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class AddFriend : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        val user_name:TextView=findViewById(R.id.Search_user)
        val bt:Button=findViewById(R.id.button2)
        val fb= FirebaseDatabase.getInstance().getReference().child(Local_user.name).child("Friends")
        bt.setOnClickListener{
            val user_base=FirebaseDatabase.getInstance().getReference().child("Users")
            val t_name=user_name.text.toString()
            user_base.addChildEventListener(object : ChildEventListener{
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    try {

                        var z: UserModel? = dataSnapshot.getValue(UserModel::class.java) ?: return

                        if(z!!.name==t_name){
                            val tb= FirebaseDatabase.getInstance().getReference().child(t_name).child("Friends")

                            var new_f=friend()
                            new_f.name=t_name
                            new_f.msg=""
                            new_f.num=0
                            fb.child(t_name).setValue(new_f)
                            new_f.name= Local_user.name
                            new_f.msg=""
                            new_f.num=0
                            tb.child(Local_user.name).setValue(new_f)
                            Toast.makeText(
                                baseContext, "Add friends successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }


                    }
                    catch (e:Exception){
                        return
                    }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {           }
                override fun onChildRemoved(snapshot: DataSnapshot) {            }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {            }
                override fun onCancelled(error: DatabaseError) {            }

            })

        }
    }

}