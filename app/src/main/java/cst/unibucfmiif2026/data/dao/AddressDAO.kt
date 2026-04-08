package cst.unibucfmiif2026.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import cst.unibucfmiif2026.data.entities.AddressEntity
@Dao
interface AddressDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(address: AddressEntity) : Long

    @Delete
    suspend fun delete(address: AddressEntity)
}