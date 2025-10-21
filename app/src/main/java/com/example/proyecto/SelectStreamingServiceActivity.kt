package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SelectStreamingServiceActivity : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_streaming_service)

        userId = intent.getIntExtra("USER_ID", -1)

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener { 
            finish() 
        }

        findViewById<Button>(R.id.netflix_button).setOnClickListener { openPlanSelection("Netflix") }
        findViewById<Button>(R.id.prime_video_button).setOnClickListener { openPlanSelection("Amazon Prime Video") }
        findViewById<Button>(R.id.disney_plus_button).setOnClickListener { openPlanSelection("Disney+") }
        findViewById<Button>(R.id.hbo_max_button).setOnClickListener { openPlanSelection("HBO Max") }
        findViewById<Button>(R.id.paramount_plus_button).setOnClickListener { openPlanSelection("Paramount+") }
        findViewById<Button>(R.id.vix_premium_button).setOnClickListener { openPlanSelection("Vix Premium") }
        findViewById<Button>(R.id.apple_tv_plus_button).setOnClickListener { openPlanSelection("Apple TV+") }
    }

    private fun openPlanSelection(serviceName: String) {
        val intent = Intent(this, SelectPlanActivity::class.java)
        intent.putExtra("SERVICE_NAME", serviceName)
        intent.putExtra("CATEGORY", "Streaming")
        intent.putExtra("USER_ID", userId) // Pass the user ID
        startActivity(intent)
    }
}