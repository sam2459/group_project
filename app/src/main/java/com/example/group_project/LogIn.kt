package com.example.group_project
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.group_project.ui.Chat.UserView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase



class LogIn : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        databaseReference= FirebaseDatabase.getInstance().getReference()
        //firebaseUser = auth!!.currentUser!!

        //check if user login then navigate to user screen

        val btnSignUp: Button = findViewById(R.id.btnSignUp)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val etName: EditText =findViewById(R.id.etName)
        val etPassword: EditText =findViewById(R.id.etPassword)
        btnLogin.setOnClickListener {
            val name = etName.text.toString()
            val password = etPassword.text.toString()

            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(password)) {
                Toast.makeText(
                    applicationContext,
                    "name and password are required",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                databaseReference.child("Users").child(name).child("pwd").get().addOnSuccessListener {
                    Log.d("A",password+' '+it.value)
                    if(password==it.value) {
                        etName.setText("")
                        etPassword.setText("")
                        Toast.makeText(
                            applicationContext,
                            "Log in successful!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Local_user.name =name
                        val intent = Intent(
                            this@LogIn,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(
                            applicationContext,
                            "Wrong password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                        }
                    .addOnFailureListener{
                            Toast.makeText(
                                applicationContext,
                                "No such user!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

            }
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(
                this@LogIn,
                SignUp::class.java
            )

            startActivity(intent)
            finish()
        }
    }
}