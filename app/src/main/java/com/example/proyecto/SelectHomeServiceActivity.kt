package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SelectHomeServiceActivity : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_home_service)

        userId = intent.getIntExtra("USER_ID", -1)

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener { 
            finish()
        }

        findViewById<Button>(R.id.internet_cable_button).setOnClickListener { openAddSubscription("Internet y Cable") }
        findViewById<Button>(R.id.water_button).setOnClickListener { openAddSubscription("Agua") }
        findViewById<Button>(R.id.electricity_button).setOnClickListener { openAddSubscription("Electricidad") }
        findViewById<Button>(R.id.gas_button).setOnClickListener { openAddSubscription("Gas") }
    }

    private fun openAddSubscription(serviceName: String) {
        val intent = Intent(this, AddSubscriptionActivity::class.java)
        intent.putExtra("SERVICE_NAME", serviceName)
        intent.putExtra("CATEGORY", "Servicios de casa")
        intent.putExtra("USER_ID", userId) // Pass the user ID
        startActivity(intent)
    }
}