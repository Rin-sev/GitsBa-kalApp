package com.example.kalapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "triage_messages")
data class TriageMessageEntity(
    @PrimaryKey
    val id: String,
    val houseId: String,
    val householdName: String,
    val status: String,         // stored as string
    val timestamp: Long,
    val acknowledged: Boolean
)


fun TriageMessageEntity.toDomain(): TriageMessage {
    return TriageMessage(
        id            = id,
        houseId       = houseId,
        householdName = householdName,
        status        = TriageStatus.valueOf(status),
        timestamp     = java.time.Instant.ofEpochMilli(timestamp)
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime(),
        acknowledged  = acknowledged
    )
}


fun TriageMessage.toEntity(): TriageMessageEntity {
    return TriageMessageEntity(
        id            = id,
        houseId       = houseId,
        householdName = householdName,
        status        = status.name,
        timestamp     = timestamp
            .atZone(java.time.ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli(),
        acknowledged  = acknowledged
    )
}