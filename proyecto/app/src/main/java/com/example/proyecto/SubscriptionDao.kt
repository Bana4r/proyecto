package com.example.proyecto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {
    @Insert
    suspend fun insert(subscription: Subscription)

    @Query("SELECT * FROM subscriptions WHERE userId = :userId")
    fun getAllSubscriptions(userId: Int): Flow<List<Subscription>>

    @Delete
    suspend fun delete(subscription: Subscription)

    @Update
    suspend fun update(subscription: Subscription)
}