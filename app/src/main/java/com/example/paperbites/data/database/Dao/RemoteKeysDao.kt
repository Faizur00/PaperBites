package com.example.paperbites.data.database.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paperbites.data.database.Entity.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeys)

    @Query("SELECT * FROM remote_keys WHERE filterId = :filterId")
    suspend fun remoteKeysByFilter(filterId: String): RemoteKeys?

    @Query("DELETE FROM remote_keys WHERE filterId = :filterId")
    suspend fun deleteByFilter(filterId: String)
}
