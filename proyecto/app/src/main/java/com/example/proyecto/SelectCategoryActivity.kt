package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SelectCategoryActivity : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_category)

        userId = intent.getIntExtra("USER_ID", -1)

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener { 
            finish()
        }

        findViewById<Button>(R.id.streaming_button).setOnClickListener { 
            val intent = Intent(this, SelectStreamingServiceActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
        findViewById<Button>(R.id.music_button).setOnClickListener { 
            val intent = Intent(this, SelectMusicServiceActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
        findViewById<Button>(R.id.games_button).setOnClickListener { 
            val intent = Intent(this, SelectGameServiceActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
        findViewById<Button>(R.id.home_services_button).setOnClickListener { 
            val intent = Intent(this, SelectHomeServiceActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
        findViewById<Button>(R.id.external_service_button).setOnClickListener { 
            val intent = Intent(this, AddExternalServiceActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
    }
}