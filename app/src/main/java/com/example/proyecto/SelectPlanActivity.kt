package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SelectPlanActivity : AppCompatActivity() {

    private lateinit var serviceName: String
    private lateinit var category: String
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_plan)

        findViewById<ImageView>(R.id.back_arrow).setOnClickListener { 
            finish()
        }

        userId = intent.getIntExtra("USER_ID", -1)
        serviceName = intent.getStringExtra("SERVICE_NAME") ?: return
        category = intent.getStringExtra("CATEGORY") ?: return

        val plans = getPlansForService(serviceName)
        val planGroups = groupPlans(plans)

        val recyclerView = findViewById<RecyclerView>(R.id.plans_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlanAdapter(planGroups) { planGroup ->
            if (planGroup.plans.size == 1) {
                openAddSubscription(planGroup.plans.first())
            } else {
                showPlanOptionsDialog(planGroup)
            }
        }
    }

    private fun showPlanOptionsDialog(planGroup: PlanGroup) {
        val options = planGroup.plans.map { String.format("$%.2f / %s", it.price, it.billingPeriod) }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Selecciona una opción para ${planGroup.name}")
            .setItems(options) { dialog, which ->
                val selectedPlan = planGroup.plans[which]
                openAddSubscription(selectedPlan)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openAddSubscription(plan: Plan) {
        val intent = Intent(this, AddSubscriptionActivity::class.java)
        intent.putExtra("CATEGORY", category)
        intent.putExtra("SERVICE_NAME", serviceName)
        intent.putExtra("PLAN", plan)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }

    private fun groupPlans(plans: List<Plan>): List<PlanGroup> {
        return plans.groupBy { it.name }
            .map { (name, planList) -> PlanGroup(name, planList) }
    }

    private fun getPlansForService(serviceName: String): List<Plan> {
        return when (serviceName) {
            // Streaming Services
            "Netflix" -> listOf(
                Plan("Estándar con Anuncios", 119.0, "mensual"),
                Plan("Estándar", 249.0, "mensual"),
                Plan("Premium", 329.0, "mensual")
            )
            "Amazon Prime Video" -> listOf(
                Plan("Estándar con Anuncios", 99.0, "mensual"),
                Plan("Estándar con Anuncios", 899.0, "anual"),
                Plan("Sin Anuncios", 149.0, "mensual")
            )
            "Disney+" -> listOf(
                Plan("Estándar con Anuncios", 99.0, "mensual"),
                Plan("Estándar", 249.0, "mensual"),
                Plan("Estándar", 2089.0, "anual"),
                Plan("Premium", 319.0, "mensual"),
                Plan("Premium", 2679.0, "anual")
            )
            "HBO Max" -> listOf(
                Plan("Básico con Anuncios", 149.0, "mensual"),
                Plan("Básico con Anuncios", 1428.0, "anual"),
                Plan("Estándar", 239.0, "mensual"),
                Plan("Estándar", 2148.0, "anual"),
                Plan("Platino", 299.0, "mensual"),
                Plan("Platino", 2868.0, "anual")
            )
            "Paramount+" -> listOf(
                Plan("Básico", 79.0, "mensual"),
                Plan("Estándar", 109.0, "mensual"),
                Plan("Premium", 179.0, "mensual")
            )
            "Vix Premium" -> listOf(
                Plan("Vix Premium", 119.0, "mensual"),
                Plan("Vix Premium", 999.0, "anual")
            )
            "Apple TV+" -> listOf(
                Plan("Apple TV+", 129.0, "mensual")
            )

            // Music Services
            "Spotify" -> listOf(
                Plan("Individual", 139.0, "mensual"),
                Plan("Dúo", 189.0, "mensual"),
                Plan("Familiar", 239.0, "mensual"),
                Plan("Estudiantes", 74.0, "mensual")
            )
            "Amazon Music" -> listOf(
                Plan("Individual", 129.0, "mensual"),
                Plan("Familiar", 199.0, "mensual"),
                Plan("Echo/Fire TV", 69.0, "mensual")
            )
            "Apple Music" -> listOf(
                Plan("Individual", 129.0, "mensual"),
                Plan("Familiar", 199.0, "mensual"),
                Plan("Estudiantes", 69.0, "mensual")
            )
            "Deezer" -> listOf(
                Plan("Premium", 139.0, "mensual"),
                Plan("Premium", 1240.0, "anual"),
                Plan("Familiar", 239.0, "mensual"),
                Plan("Familiar", 2620.0, "anual")
            )
            "YouTube Music Premium" -> listOf(
                Plan("Individual", 115.0, "mensual"),
                Plan("Familiar", 179.0, "mensual")
            )

            // Game Services
            "Xbox Game Pass" -> listOf(
                Plan("Essential", 169.0, "mensual"),
                Plan("PC", 299.0, "mensual"),
                Plan("Ultimate", 449.0, "mensual")
            )
            "PlayStation Plus" -> listOf(
                Plan("Essential", 147.0, "mensual"),
                Plan("Essential", 992.0, "anual"),
                Plan("Extra", 220.0, "mensual"),
                Plan("Extra", 1984.0, "anual"),
                Plan("Deluxe", 257.0, "mensual"),
                Plan("Deluxe", 2297.0, "anual")
            )
            "Nintendo Switch Online" -> listOf(
                Plan("Individual", 479.0, "anual"),
                Plan("Familiar", 1199.0, "anual")
            )

            else -> emptyList()
        }
    }
}