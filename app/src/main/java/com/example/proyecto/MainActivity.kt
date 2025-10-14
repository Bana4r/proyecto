package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login)

        loginButton.setOnClickListener {
            if (email.text.toString() == "admin@admin.com" && password.text.toString() == "admin") {
                startActivity(Intent(this, WelcomeActivity::class.java))
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}