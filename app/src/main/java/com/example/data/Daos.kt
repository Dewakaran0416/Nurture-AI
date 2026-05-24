package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}

@Dao
interface SleepDao {
    @Query("SELECT * FROM sleep_logs ORDER BY startTime DESC")
    fun getAllSleepLogs(): Flow<List<SleepLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSleep(log: SleepLog)

    @Delete
    suspend fun deleteSleep(log: SleepLog)
}

@Dao
interface VaccineDao {
    @Query("SELECT * FROM vaccine_logs ORDER BY dueDate ASC")
    fun getAllVaccines(): Flow<List<VaccineLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccines(vaccines: List<VaccineLog>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccine(vaccine: VaccineLog)

    @Update
    suspend fun updateVaccine(vaccine: VaccineLog)
}

@Dao
interface CryingDao {
    @Query("SELECT * FROM crying_logs ORDER BY timestamp DESC")
    fun getAllCryingLogs(): Flow<List<CryingLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrying(log: CryingLog)

    @Delete
    suspend fun deleteCrying(log: CryingLog)
}

@Dao
interface FeedingDao {
    @Query("SELECT * FROM feeding_logs ORDER BY timestamp DESC")
    fun getAllFeedingLogs(): Flow<List<FeedingLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeding(log: FeedingLog)

    @Delete
    suspend fun deleteFeeding(log: FeedingLog)
}

@Dao
interface MilestoneDao {
    @Query("SELECT * FROM milestones ORDER BY targetAgeMonths ASC")
    fun getAllMilestones(): Flow<List<Milestone>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(milestones: List<Milestone>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: Milestone)

    @Update
    suspend fun updateMilestone(milestone: Milestone)
}

@Dao
interface MemoryDao {
    @Query("SELECT * FROM memories ORDER BY timestamp DESC")
    fun getAllMemories(): Flow<List<Memory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(memory: Memory)

    @Delete
    suspend fun deleteMemory(memory: Memory)
}
