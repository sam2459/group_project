package com.example.group_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.group_project.ui.Chat.UserModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUp : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val btnSignUp:Button = findViewById(R.id.btnSignUp)
        val btnLogin:Button = findViewById(R.id.btnLogin)
        val etName:EditText=findViewById(R.id.etName)
        val etEmail:EditText=findViewById(R.id.etEmail)
        val etPassword:EditText=findViewById(R.id.etPassword)
        val etConfirmPassword:EditText=findViewById(R.id.etConfirmPassword)

        databaseReference= FirebaseDatabase.getInstance().getReference()


        btnSignUp.setOnClickListener {
            val userName = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            if (TextUtils.isEmpty(userName)){
                Toast.makeText(applicationContext,"username is required", Toast.LENGTH_SHORT).show()

            }
            else if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"email is required", Toast.LENGTH_SHORT).show()
            }

            else if (TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext,"password is required", Toast.LENGTH_SHORT).show()
            }

            else if (TextUtils.isEmpty(confirmPassword)){
                Toast.makeText(applicationContext,"confirm password is required", Toast.LENGTH_SHORT).show()
            }

            else if (!password.equals(confirmPassword)){
                Toast.makeText(applicationContext,"password not match", Toast.LENGTH_SHORT).show()
            }
            else registerUser(userName,email,password)

        }

        btnLogin.setOnClickListener {
            val intent = Intent(this@SignUp,
                LogIn::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun registerUser(userName:String,email:String,password:String) {
        var z= UserModel()
        z.name=userName
        z.pwd=password
        z.email=email
        databaseReference.child("Users").child(userName).setValue(z)
        Local_user.name =userName
        Toast.makeText(applicationContext,"Sign up successfully!", Toast.LENGTH_SHORT).show()
        val intent = Intent(
            this@SignUp,
            MainActivity::class.java
        )
        startActivity(intent)
        finish()
    }
}
