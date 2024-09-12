package com.bs.sriwilispetugas.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "login_response_table")
data class LoginResponseEntity(
	@PrimaryKey val id: Int,
	val success: Boolean?,
	val message: String?,
	val access_token: String?,
	val nama_petugas: String?
)
