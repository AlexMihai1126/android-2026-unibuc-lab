package cst.unibucfmiif2026.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cst.unibucfmiif2026.data.entities.AddressEntity
import cst.unibucfmiif2026.data.entities.AddressWithUser
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(address: AddressEntity) : Long

    @Delete
    suspend fun delete(address: AddressEntity)

    @Query("SELECT * FROM addresses WHERE ${AddressEntity.ARG_ID} = :id")
    suspend fun getById(id: Long) : AddressWithUser?

    @Query("SELECT * FROM addresses")
    fun getAll() : Flow<List<AddressEntity>>
}