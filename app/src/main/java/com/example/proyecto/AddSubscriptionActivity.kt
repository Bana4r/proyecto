package com.example.proyecto

//imports necesarios para el funcionamiento del proyecto
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddSubscriptionActivity : AppCompatActivity() {

    private lateinit var subscriptionDao: SubscriptionDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subscription)

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener { 
            finish()
        }

        subscriptionDao = AppDatabase.getDatabase(this).subscriptionDao()

        val userId = intent.getIntExtra("USER_ID", -1)
        val category = intent.getStringExtra("CATEGORY")
        val serviceName = intent.getStringExtra("SERVICE_NAME")
        val plan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("PLAN", Plan::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("PLAN")
        }

        if (userId == -1 || category == null || serviceName == null || plan == null) {
            Toast.makeText(this, "Error: Faltan datos de la suscripción.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val serviceNameTextView = findViewById<TextView>(R.id.service_name_text_view)
        val planDetailsTextView = findViewById<TextView>(R.id.plan_details_text_view)
        val saveButton = findViewById<Button>(R.id.save_button)

        serviceNameTextView.text = serviceName
        planDetailsTextView.text = String.format("%s - $%.2f / %s", plan.name, plan.price, plan.billingPeriod)

        saveButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            if (plan.billingPeriod == "mensual") {
                calendar.add(Calendar.MONTH, 1)
            } else if (plan.billingPeriod == "anual") {
                calendar.add(Calendar.YEAR, 1)
            }
            val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
            val renewalDate = dateFormat.format(calendar.time)

            val subscription = Subscription(
                userId = userId, // Associate with the current user
                serviceName = serviceName,
                planName = plan.name,
                renewalDate = renewalDate,
                cost = plan.price,
                category = category
            )

            lifecycleScope.launch {
                subscriptionDao.insert(subscription)
                runOnUiThread {
                    Toast.makeText(this@AddSubscriptionActivity, "Suscripción guardada", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@AddSubscriptionActivity, WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("USER_ID", userId) // Pass the user ID back
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}