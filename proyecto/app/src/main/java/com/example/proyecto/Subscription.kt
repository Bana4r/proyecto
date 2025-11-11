package com.example.proyecto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
// Modificamos el apartado de suscripcion
@Entity(tableName = "subscriptions",
        foreignKeys = [ForeignKey(entity = User::class,
                                  parentColumns = ["id"],
                                  childColumns = ["userId"],
                                  onDelete = ForeignKey.CASCADE)])
data class Subscription(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Foreign key to link to the User table
    val serviceName: String,
    val planName: String,
    val renewalDate: String,
    val cost: Double,
    val category: String,
    var isActive: Boolean = true
)
