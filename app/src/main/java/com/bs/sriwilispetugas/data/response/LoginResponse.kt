package com.bs.sriwilispetugas.data.response


data class LoginResponseDTO(
	val success: Boolean?,
	val message: String?,
	val dataPetugas: DataPetugasDTO?
)

data class DataPetugasDTO(
	val access_token: String?,
	val nama_petugas: String?
)
