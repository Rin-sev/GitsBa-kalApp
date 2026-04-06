package com.example.kalapp.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// HOUSEHOLD STRUCTURE
data class Household (
    val id   : String,
    val name : String
)


// MESSAGE SCHEMA
data class TriageMessage (
    val id : String = java.util.UUID.randomUUID().toString(),
    val houseId : String,
    val householdName : String,
    val status : TriageStatus,
    val timestamp : LocalDateTime = LocalDateTime.now(),
    val acknowledged : Boolean = false
) {
    val formattedDate : String
        get() = timestamp.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))

    val formattedTime : String
        get() = timestamp.format(DateTimeFormatter.ofPattern("hh:mm a"))
}


enum class TriageStatus(val label : String ) {
    URGENT("URGENT"),
    EVACUATED("EVACUATED"),
    SAFE("SAFE")
}


// hardcoded list of household names
val sampleHouseholds = listOf(
    Household("SRQ-001", "Dela Cruz"),
    Household("SRQ-002", "Reyes")
)