package com.shishusneh.app.data.db

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────────────────────────────────────
//  ENTITIES
// ─────────────────────────────────────────────────────────────────────────────

/** One breastfeed session stored in Room */
@Entity(tableName = "breastfeed_sessions")
data class BreastfeedSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val leftMinutes: Int,
    val leftSeconds: Int,
    val rightMinutes: Int,
    val rightSeconds: Int,
    val estimatedMl: Int,
    val noteTag: String,
    val timestampMs: Long = System.currentTimeMillis()
)

/** One bottle session stored in Room */
@Entity(tableName = "bottle_sessions")
data class BottleSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contentType: String,   // "FORMULA" | "BREAST_MILK" | "WATER"
    val amountOz: Float,
    val amountMl: Int,
    val durationMinutes: Int,
    val timestampMs: Long = System.currentTimeMillis()
)

/** One growth measurement stored in Room */
@Entity(tableName = "growth_records")
data class GrowthRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val metric: String,       // "WEIGHT" | "HEIGHT" | "HEAD"
    val value: Float,         // e.g. 5600 (grams) or 60.0 (cm)
    val unit: String,         // "g" | "cm"
    val timestampMs: Long = System.currentTimeMillis()
)

// ─────────────────────────────────────────────────────────────────────────────
//  DAOs
// ─────────────────────────────────────────────────────────────────────────────

@Dao
interface BreastfeedDao {
    @Insert
    suspend fun insert(session: BreastfeedSessionEntity): Long

    @Query("SELECT * FROM breastfeed_sessions ORDER BY timestampMs DESC")
    fun getAllFlow(): Flow<List<BreastfeedSessionEntity>>

    @Query("SELECT * FROM breastfeed_sessions ORDER BY timestampMs DESC LIMIT 50")
    suspend fun getRecent(): List<BreastfeedSessionEntity>

    @Delete
    suspend fun delete(session: BreastfeedSessionEntity)
}

@Dao
interface BottleDao {
    @Insert
    suspend fun insert(session: BottleSessionEntity): Long

    @Query("SELECT * FROM bottle_sessions ORDER BY timestampMs DESC")
    fun getAllFlow(): Flow<List<BottleSessionEntity>>

    @Query("SELECT * FROM bottle_sessions ORDER BY timestampMs DESC LIMIT 50")
    suspend fun getRecent(): List<BottleSessionEntity>

    @Delete
    suspend fun delete(session: BottleSessionEntity)
}

@Dao
interface GrowthDao {
    @Insert
    suspend fun insert(record: GrowthRecordEntity): Long

    @Query("SELECT * FROM growth_records WHERE metric = :metric ORDER BY timestampMs DESC")
    fun getByMetricFlow(metric: String): Flow<List<GrowthRecordEntity>>

    @Query("SELECT * FROM growth_records WHERE metric = :metric ORDER BY timestampMs DESC")
    suspend fun getByMetric(metric: String): List<GrowthRecordEntity>

    @Query("SELECT * FROM growth_records ORDER BY timestampMs DESC")
    fun getAllFlow(): Flow<List<GrowthRecordEntity>>

    @Delete
    suspend fun delete(record: GrowthRecordEntity)
}

// ─────────────────────────────────────────────────────────────────────────────
//  DATABASE
// ─────────────────────────────────────────────────────────────────────────────

@Database(
    entities = [BreastfeedSessionEntity::class, BottleSessionEntity::class, GrowthRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShishuDatabase : RoomDatabase() {
    abstract fun breastfeedDao(): BreastfeedDao
    abstract fun bottleDao(): BottleDao
    abstract fun growthDao(): GrowthDao

    companion object {
        @Volatile private var INSTANCE: ShishuDatabase? = null

        fun getInstance(context: Context): ShishuDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ShishuDatabase::class.java,
                    "shishu_sneh.db"
                ).build().also { INSTANCE = it }
            }
    }
}
