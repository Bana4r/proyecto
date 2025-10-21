package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SelectMusicServiceActivity : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_music_service)

        userId = intent.getIntExtra("USER_ID", -1)

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener { 
            finish()
        }

        findViewById<Button>(R.id.spotify_button).setOnClickListener { openPlanSelection("Spotify") }
        findViewById<Button>(R.id.amazon_music_button).setOnClickListener { openPlanSelection("Amazon Music") }
        findViewById<Button>(R.id.apple_music_button).setOnClickListener { openPlanSelection("Apple Music") }
        findViewById<Button>(R.id.deezer_button).setOnClickListener { openPlanSelection("Deezer") }
        findViewById<Button>(R.id.youtube_music_button).setOnClickListener { openPlanSelection("YouTube Music Premium") }
    }

    private fun openPlanSelection(serviceName: String) {
        val intent = Intent(this, SelectPlanActivity::class.java)
        intent.putExtra("SERVICE_NAME", serviceName)
        intent.putExtra("CATEGORY", "Música")
        intent.putExtra("USER_ID", userId) // Pass the user ID
        startActivity(intent)
    }
}