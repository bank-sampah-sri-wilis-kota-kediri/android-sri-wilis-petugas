package com.bs.sriwilispetugas.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nasabah_table")
data class NasabahEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama_nasabah: String?,
    val no_hp_nasabah: String?,
    val alamat_nasabah: String?,
    val saldo_nasabah: String?,
    val jasa: String?,
    val is_dapat_jasa: String?,
    val gambar_nasabah: String?,
    val created_at: String?,
    val updated_at: String?
)