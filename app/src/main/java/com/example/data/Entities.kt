package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val displayName: String,
    val profilePic: String = "",
    val pinHash: String = "" // Simple pass/pin verification stored offline
)

@Entity(tableName = "sleep_logs")
data class SleepLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long,
    val endTime: Long?,
    val quality: Int, // 1 to 5 stars
    val note: String
)

@Entity(tableName = "vaccine_logs")
data class VaccineLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val targetAge: String,
    val dueDate: Long,
    val status: String, // PENDING, COMPLETED
    val completedDate: Long? = null,
    val note: String = ""
)

@Entity(tableName = "crying_logs")
data class CryingLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val description: String,
    val audioDurationSec: Int = 0,
    val suspectedReason: String, // Hunger, Tired, Wet Diaper, Gas, etc.
    val severity: String, // Low, Medium, High
    val aiAnalysis: String? = null // AI interpretation of crying reasons & tips
)

@Entity(tableName = "feeding_logs")
data class FeedingLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String, // BREAST, FORMULA, SOLIDS
    val amountOrDuration: String, // e.g. "120ml", "15 minutes"
    val foodDetails: String = "", // e.g., "Apple Purée", "Oatmeal"
    val note: String = ""
)

@Entity(tableName = "milestones")
data class Milestone(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Social, Language, Motor, Cognitive
    val targetAgeMonths: Int,
    val status: String, // COMPENDING, COMPLETED
    val completedDate: Long? = null,
    val note: String = ""
)

@Entity(tableName = "memories")
data class Memory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val localImageUri: String? = null,
    val isFavorite: Boolean = false
)
