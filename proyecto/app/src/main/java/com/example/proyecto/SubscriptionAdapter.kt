package com.example.proyecto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SubscriptionAdapter(private val onActionClick: (Subscription) -> Unit) : ListAdapter<Subscription, SubscriptionAdapter.SubscriptionViewHolder>(SubscriptionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subscription_item, parent, false)
        return SubscriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        holder.bind(getItem(position), onActionClick)
    }

    class SubscriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serviceName: TextView = itemView.findViewById(R.id.service_name)
        private val planName: TextView = itemView.findViewById(R.id.plan_name)
        private val cost: TextView = itemView.findViewById(R.id.cost)
        private val deleteIcon: ImageView = itemView.findViewById(R.id.delete_icon)

        fun bind(subscription: Subscription, onActionClick: (Subscription) -> Unit) {
            serviceName.text = subscription.serviceName
            cost.text = String.format("$%.2f", subscription.cost)

            if (subscription.isActive) {
                planName.text = String.format("%s se renueva el %s", subscription.planName, subscription.renewalDate)
                deleteIcon.setImageResource(android.R.drawable.ic_menu_delete) // Standard delete icon
            } else {
                planName.text = String.format("Vigente hasta el %s", subscription.renewalDate)
                deleteIcon.setImageResource(android.R.drawable.ic_menu_close_clear_cancel) // A more "permanent" delete icon
            }

            deleteIcon.setOnClickListener { onActionClick(subscription) }
        }
    }
}

class SubscriptionDiffCallback : DiffUtil.ItemCallback<Subscription>() {
    override fun areItemsTheSame(oldItem: Subscription, newItem: Subscription):
        Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Subscription, newItem: Subscription):
        Boolean = oldItem == newItem
}