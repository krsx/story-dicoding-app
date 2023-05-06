package com.example.proyekakhirstoryapp.data.db.remotekey

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKey(id: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAllRemoteKeys()

}