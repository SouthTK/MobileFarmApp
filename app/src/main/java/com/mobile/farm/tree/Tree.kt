package com.mobile.farm.tree

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
import kotlinx.coroutines.flow.Flow

@Serializable
object TreesList

@Serializable
data class TreeDetails(val treeId: Int)

@Entity(tableName = "tree")
data class Tree(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

@Dao
interface TreeDao {
    @Query("SELECT * FROM tree ORDER BY id")
    fun getAll(): Flow<List<Tree>>

//    @Query("SELECT * FROM tree WHERE age > :minAge")
//    fun getAdults(minAge: Int): Flow<List<Tree>>

    @Query("SELECT * FROM tree WHERE id = :treeId")
    suspend fun getById(treeId: Int): Tree?

    @Query("SELECT COUNT(*) FROM tree")
    suspend fun count(): Int

    @Insert
    suspend fun insertAll(trees: List<Tree>)
}

@Database(entities = [Tree::class], version = 1, exportSchema = false)
abstract class TreesDatabase : RoomDatabase() {
    abstract fun treeDao(): TreeDao

    companion object {
        @Volatile
        private var instance: TreesDatabase? = null

        fun getInstance(context: Context): TreesDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TreesDatabase::class.java,
                    "trees.db"
                ).build().also { instance = it }
            }
        }
    }
}