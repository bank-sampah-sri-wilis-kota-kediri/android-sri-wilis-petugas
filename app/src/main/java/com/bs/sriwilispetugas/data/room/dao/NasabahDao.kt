package com.bs.sriwilispetugas.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.bs.sriwilispetugas.data.room.NasabahEntity

@Dao
interface NasabahDao {

    // Insert a single NasabahApiResponseEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNasabah(nasabah: NasabahEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNasabah(nasabahList: List<NasabahEntity>)

    // Get all Nasabah from the table
    @Query("SELECT * FROM nasabah_table")
    suspend fun getAllNasabah(): List<NasabahEntity>

    // Get a single Nasabah by its ID
    @Query("SELECT * FROM nasabah_table WHERE id = :id")
    suspend fun getNasabahById(id: Int): NasabahEntity?

    // Update a Nasabah
    @Update
    suspend fun updateNasabah(nasabah: NasabahEntity)

    // Delete a Nasabah
    @Delete
    suspend fun deleteNasabah(nasabah: NasabahEntity)

    // Delete all Nasabah records
    @Query("DELETE FROM nasabah_table")
    suspend fun deleteAllNasabah()
}