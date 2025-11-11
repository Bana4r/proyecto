package com.example.proyecto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plan(
    val name: String,
    val price: Double,
    val billingPeriod: String // "mensual" or "anual"
) : Parcelable

data class PlanGroup(
    val name: String,
    val plans: List<Plan>
)
