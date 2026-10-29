package com.mobile.farm.visit

import android.content.Context
import androidx.room.Dao
import kotlinx.serialization.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobile.farm.tree.Tree
import kotlinx.coroutines.flow.Flow

@Serializable
object VisitsList

@Serializable
data class VisitDetails(val date : Int)

@Entity(tableName = "visit")
data class Visit(
    @PrimaryKey
    val date: Int
)

@Dao
interface VisitDao{
    @Query("SELECT * FROM visit ORDER BY date")
    fun getAll(): Flow<List<Visit>>

    @Query("SELECT * FROM visit WHERE date >= :startDate AND date <= :endDate")
    fun getByRange(startDate: Int, endDate: Int) : Flow<List<Visit>>

    @Query("SELECT * FROM visit WHERE date = :visitDate")
    suspend fun getByDate(visitDate: Int) : Visit?

    @Query("SELECT COUNT(*) FROM visit")
    suspend fun count() : Int

    @Insert
    suspend fun insertAll(visits: List<Visit>)
}

@Database(entities = [Visit::class], version = 1, exportSchema = false)
abstract class VisitDatabase : RoomDatabase() {
    abstract fun visitDao() : VisitDao

    companion object {
        @Volatile
        private var instance: VisitDatabase? = null

        fun getInstance(context: Context): VisitDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    VisitDatabase::class.java,
                    "visits.db"
                ).build().also {instance = it}
            }
        }
    }
}