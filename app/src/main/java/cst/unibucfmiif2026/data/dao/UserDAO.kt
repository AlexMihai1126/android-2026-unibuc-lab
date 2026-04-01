package cst.unibucfmiif2026.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cst.unibucfmiif2026.data.entities.UserEntity

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("""
        SELECT * FROM users WHERE id = :id
    """)
    fun getById(id: Long)
}