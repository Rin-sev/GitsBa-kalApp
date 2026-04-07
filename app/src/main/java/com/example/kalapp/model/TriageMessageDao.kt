package com.example.kalapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TriageMessageDao {

    // Observe all messages
    @Query("SELECT * FROM triage_messages ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<TriageMessageEntity>>

    // Insert or update a message
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: TriageMessageEntity)

    // Mark a message as acknowledged
    @Query("UPDATE triage_messages SET acknowledged = 1 WHERE id = :id")
    suspend fun acknowledge(id: String): Int
}