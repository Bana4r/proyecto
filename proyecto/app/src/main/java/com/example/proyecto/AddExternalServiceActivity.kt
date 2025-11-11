package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddExternalServiceActivity : AppCompatActivity() {

    private lateinit var subscriptionDao: SubscriptionDao
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_external_service)

        userId = intent.getIntExtra("USER_ID", -1)

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener { 
            finish()
        }

        subscriptionDao = AppDatabase.getDatabase(this).subscriptionDao()

        // Setup Spinners
        val categories = listOf("Streaming", "Música", "Juegos")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        findViewById<AutoCompleteTextView>(R.id.category_auto_complete).setAdapter(categoryAdapter)

        val billingPeriods = listOf("mensual", "anual")
        val billingPeriodAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, billingPeriods)
        findViewById<AutoCompleteTextView>(R.id.billing_period_auto_complete).setAdapter(billingPeriodAdapter)

        val serviceNameEditText = findViewById<TextInputEditText>(R.id.service_name_edit_text)
        val costEditText = findViewById<TextInputEditText>(R.id.cost_edit_text)
        val renewalDateEditText = findViewById<TextInputEditText>(R.id.renewal_date_edit_text)
        val categoryAutoComplete = findViewById<AutoCompleteTextView>(R.id.category_auto_complete)
        val billingPeriodAutoComplete = findViewById<AutoCompleteTextView>(R.id.billing_period_auto_complete)
        val saveButton = findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener {
            val serviceName = serviceNameEditText.text.toString()
            val category = categoryAutoComplete.text.toString()
            val cost = costEditText.text.toString().toDoubleOrNull()
            val renewalDate = renewalDateEditText.text.toString()
            val billingPeriod = billingPeriodAutoComplete.text.toString()

            if (serviceName.isNotEmpty() && category.isNotEmpty() && cost != null && renewalDate.isNotEmpty() && billingPeriod.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Confirmar Suscripción")
                    .setMessage("Vas a añadir...\nServicio: $serviceName\nCategoría: $category\nCosto: $$cost / $billingPeriod\nRenovación: $renewalDate")
                    .setPositiveButton("Confirmar") { _, _ ->
                        val subscription = Subscription(
                            userId = userId,
                            serviceName = serviceName,
                            planName = "Plan Personalizado",
                            renewalDate = renewalDate,
                            cost = cost,
                            category = category
                        )
                        lifecycleScope.launch {
                            subscriptionDao.insert(subscription)
                            runOnUiThread {
                                Toast.makeText(this@AddExternalServiceActivity, "Suscripción guardada", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@AddExternalServiceActivity, WelcomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("USER_ID", userId)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            } else {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}