package com.example.proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlanAdapter(private val planGroups: List<PlanGroup>, private val onPlanGroupClick: (PlanGroup) -> Unit) : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plan_item, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(planGroups[position], onPlanGroupClick)
    }

    override fun getItemCount(): Int = planGroups.size

    class PlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val planNameTextView: TextView = itemView.findViewById(R.id.plan_name_text_view)
        private val planPriceTextView: TextView = itemView.findViewById(R.id.plan_price_text_view)
        private val moreOptionsTextView: TextView = itemView.findViewById(R.id.plan_more_options_text_view)

        fun bind(planGroup: PlanGroup, onPlanGroupClick: (PlanGroup) -> Unit) {
            planNameTextView.text = planGroup.name

            val cheapestPlan = planGroup.plans.minByOrNull { it.price }!!
            planPriceTextView.text = String.format("desde $%.2f / %s", cheapestPlan.price, cheapestPlan.billingPeriod)

            if (planGroup.plans.size > 1) {
                moreOptionsTextView.visibility = View.VISIBLE
                moreOptionsTextView.text = String.format("+%d opciones", planGroup.plans.size - 1)
            } else {
                moreOptionsTextView.visibility = View.GONE
            }

            itemView.setOnClickListener { onPlanGroupClick(planGroup) }
        }
    }
}