package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SelectGameServiceActivity : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_game_service)

        userId = intent.getIntExtra("USER_ID", -1)

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener { 
            finish()
        }

        findViewById<Button>(R.id.xbox_button).setOnClickListener { openPlanSelection("Xbox Game Pass") }
        findViewById<Button>(R.id.playstation_button).setOnClickListener { openPlanSelection("PlayStation Plus") }
        findViewById<Button>(R.id.nintendo_button).setOnClickListener { openPlanSelection("Nintendo Switch Online") }
    }

    private fun openPlanSelection(serviceName: String) {
        val intent = Intent(this, SelectPlanActivity::class.java)
        intent.putExtra("SERVICE_NAME", serviceName)
        intent.putExtra("CATEGORY", "Juegos")
        intent.putExtra("USER_ID", userId) // Pass the user ID
        startActivity(intent)
    }
}