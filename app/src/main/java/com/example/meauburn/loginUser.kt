package com.example.meauburn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class loginUser : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)
        usernameInput = findViewById(R.id.login_username) // Replace with your actual EditText ID
        loginButton = findViewById(R.id.lets_roll_btn) // Replace with your actual Button ID

        loginButton.setOnClickListener {
            val enteredUsername = usernameInput.text.toString()
            if (enteredUsername == "") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Handle incorrect username
                Toast.makeText(this, "Incorrect username", Toast.LENGTH_SHORT).show()
            }
        }
    }
}