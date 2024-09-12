package com.bs.sriwilispetugas.data.repository.mapping

import com.bs.sriwilispetugas.data.response.NasabahResponseDTO
import com.bs.sriwilispetugas.data.response.PesananSampahResponseDTO
import com.bs.sriwilispetugas.data.room.NasabahEntity
import com.bs.sriwilispetugas.data.room.PesananSampahEntity
import com.bs.sriwilispetugas.data.room.PesananSampahKeranjangEntity


class Mapping {

    fun mapPesananSampahApiResponseDtoToEntities(dto: PesananSampahResponseDTO): Pair<List<PesananSampahKeranjangEntity>, List<PesananSampahEntity>> {
        val keranjangEntities = mutableListOf<PesananSampahKeranjangEntity>()
        val sampahEntities = mutableListOf<PesananSampahEntity>()

        dto.data_keranjang.forEach { keranjang ->
            val keranjangEntity = PesananSampahKeranjangEntity(
                id_pesanan = keranjang.id_pesanan,
                id_nasabah = keranjang.id_nasabah,
                id_petugas = keranjang.id_petugas,
                nominal_transaksi = keranjang.nominal_transaksi,
                tanggal = keranjang.tanggal,
                lat = keranjang.lat,
                lng = keranjang.long,
                status_pesanan = keranjang.status_pesanan,
                created_at = keranjang.created_at,
                updated_at = keranjang.updated_at
            )
            keranjangEntities.add(keranjangEntity)

            keranjang.pesanan_sampah.forEach { sampah ->
                val sampahEntity = PesananSampahEntity(
                    id_pesanan_sampah_keranjang = keranjang.id_pesanan, // Foreign key dari keranjang
                    kategori = sampah.kategori,
                    berat_perkiraan = sampah.berat_perkiraan,
                    harga_perkiraan = sampah.harga_perkiraan,
                    gambar = sampah.gambar,
                    created_at = sampah.created_at,
                    updated_at = sampah.updated_at
                )
                sampahEntities.add(sampahEntity)
            }
        }

        return Pair(keranjangEntities, sampahEntities)
    }

    fun mapNasabahResponseDtoToEntity(dto: NasabahResponseDTO): List<NasabahEntity> {
        val nasabahEntities = mutableListOf<NasabahEntity>()

        dto.data?.forEach { nasabah ->
            val nasabahEntity = NasabahEntity(
                id = nasabah.id,
                nama_nasabah = nasabah.nama_nasabah,
                no_hp_nasabah = nasabah.no_hp_nasabah,
                alamat_nasabah = nasabah.alamat_nasabah,
                saldo_nasabah = nasabah.saldo_nasabah,
                jasa = nasabah.jasa,
                is_dapat_jasa = nasabah.is_dapat_jasa,
                gambar_nasabah = nasabah.gambar_nasabah,
                created_at = nasabah.created_at,
                updated_at = nasabah.updated_at
            )
            nasabahEntities.add(nasabahEntity)
        }

        return nasabahEntities
    }


}
