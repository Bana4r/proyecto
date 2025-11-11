package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    private lateinit var subscriptionDao: SubscriptionDao
    private lateinit var activeSubscriptionAdapter: SubscriptionAdapter
    private lateinit var inactiveSubscriptionAdapter: SubscriptionAdapter
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: No se ha podido identificar al usuario.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        subscriptionDao = AppDatabase.getDatabase(this).subscriptionDao()

        // Active Subscriptions Adapter
        activeSubscriptionAdapter = SubscriptionAdapter { subscription ->
            showCancellationConfirmationDialog(subscription)
        }
        val activeRecyclerView = findViewById<RecyclerView>(R.id.subscriptions_recycler_view)
        activeRecyclerView.adapter = activeSubscriptionAdapter
        activeRecyclerView.layoutManager = LinearLayoutManager(this)

        // Inactive Subscriptions Adapter
        inactiveSubscriptionAdapter = SubscriptionAdapter { subscription ->
            showPermanentDeleteConfirmationDialog(subscription)
        }
        val inactiveRecyclerView = findViewById<RecyclerView>(R.id.inactive_subscriptions_recycler_view)
        inactiveRecyclerView.adapter = inactiveSubscriptionAdapter
        inactiveRecyclerView.layoutManager = LinearLayoutManager(this)

        val totalMonthlyCostView = findViewById<TextView>(R.id.total_monthly_cost)
        val fabAddSubscription = findViewById<FloatingActionButton>(R.id.fab_add_subscription)
        val inactiveTitle = findViewById<TextView>(R.id.inactive_subscriptions_title)

        fabAddSubscription.setOnClickListener {
            val intent = Intent(this, SelectCategoryActivity::class.java)
            intent.putExtra("USER_ID", userId) // Pass the user ID to the next screen
            startActivity(intent)
        }

        lifecycleScope.launch {
            subscriptionDao.getAllSubscriptions(userId).collectLatest { subscriptions ->
                val activeSubscriptions = subscriptions.filter { it.isActive }
                val inactiveSubscriptions = subscriptions.filter { !it.isActive }

                activeSubscriptionAdapter.submitList(activeSubscriptions)

                if (inactiveSubscriptions.isNotEmpty()) {
                    inactiveTitle.visibility = View.VISIBLE
                    inactiveRecyclerView.visibility = View.VISIBLE
                    inactiveSubscriptionAdapter.submitList(inactiveSubscriptions)
                } else {
                    inactiveTitle.visibility = View.GONE
                    inactiveRecyclerView.visibility = View.GONE
                }

                val totalCost = activeSubscriptions.sumOf { it.cost }
                totalMonthlyCostView.text = String.format("$%.2f MXN / mes", totalCost)
            }
        }
    }

    private fun showCancellationConfirmationDialog(subscription: Subscription) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Cancelación")
            .setMessage("¿Estás seguro de que quieres cancelar la suscripción a ${subscription.serviceName}?")
            .setPositiveButton("Sí, cancelar") { _, _ ->
                cancelSubscription(subscription)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun cancelSubscription(subscription: Subscription) {
        subscription.isActive = false
        lifecycleScope.launch {
            subscriptionDao.update(subscription)
            showPostCancellationMessage(subscription)
        }
    }

    private fun showPostCancellationMessage(subscription: Subscription) {
        AlertDialog.Builder(this)
            .setTitle("Suscripción Cancelada")
            .setMessage("La suscripción a ${subscription.serviceName} ha sido cancelada. Podrás seguir disfrutando del servicio hasta el ${subscription.renewalDate}.")
            .setPositiveButton("Entendido", null)
            .show()
    }

    private fun showPermanentDeleteConfirmationDialog(subscription: Subscription) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Suscripción Permanentemente")
            .setMessage("Esta suscripción inactiva se eliminará de forma permanente. ¿Estás seguro?")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                deleteSubscriptionPermanently(subscription)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteSubscriptionPermanently(subscription: Subscription) {
        lifecycleScope.launch {
            subscriptionDao.delete(subscription)
        }
    }
}