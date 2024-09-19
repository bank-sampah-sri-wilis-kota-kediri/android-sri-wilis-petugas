package com.bs.sriwilispetugas.ui.homepage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilispetugas.adapter.ListDetailPesananAdapter
import com.bs.sriwilispetugas.databinding.ActivityPesananDetailBinding
import com.bs.sriwilispetugas.utils.ViewModelFactory
import com.bs.sriwilispetugas.helper.Result
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class PesananDetailActivity : AppCompatActivity() {

    private val pesananDetailViewModel by viewModels<PesananDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityPesananDetailBinding
    private lateinit var adapter: ListDetailPesananAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val idPesanan = intent.getStringExtra("ID_PESANAN")


        setupDetailData()
        setupRecyclerView()
        if (idPesanan != null) {
            pesananDetailViewModel.getPesananSampah(idPesanan)
        }

        binding.btnBack.setOnClickListener{
            this.onBackPressed()
        }
        binding.btnSuksesPesanan.setOnClickListener{
            if (idPesanan != null) {
                pesananDetailViewModel.changeStatusPesananSampahResponse(idPesanan, "selesai diantar")
            }
        }
        binding.btnCancelPesanan.setOnClickListener{
            if (idPesanan != null) {
                pesananDetailViewModel.changeStatusPesananSampahResponse(idPesanan, "gagal")
            }
        }

        observeViewModel()
    }

    @SuppressLint("QueryPermissionsNeeded", "SetTextI18n")
    fun setupDetailData(){
        val idPesanan = intent.getStringExtra("ID_PESANAN")

        if(idPesanan!=null){
            pesananDetailViewModel.getDataDetailPesananSampahKeranjang(idPesanan)

            pesananDetailViewModel.pesanans.observe(this) { result ->
                when(result) {
                    is Result.Loading -> {
                        binding.progressBarDetail.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBarDetail.visibility = View.GONE
                        val detail = result.data
                        binding.tvNamaDetailPesanan.text = detail.nama_nasabah
                        binding.tvTanggalDetailPesanan.text = convertDateToText(detail.tanggal)
                        binding.tvBeratDetailPesanan.text = "${detail.total_berat} Kg"
                        binding.tvNomorwaDetailPesanan.text = detail.no_hp_nasabah

                        binding.btnMapsDetailPesanan.setOnClickListener {
                            openMaps(detail.lat.toDouble(), detail.lng.toDouble())
                        }
                        binding.tvAlamatDetailPesanan.text = detail.alamat_nasabah


                    }
                    is Result.Error -> {
                        binding.progressBarDetail.visibility = View.GONE
                        Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun convertDateToText(date: String): String {
        // Example function to convert date to a string with month name
        // Assuming input date format is "yyyy-MM-dd"
        val months = arrayOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus",
            "September", "Oktober", "November", "Desember"
        )
        val parts = date.split("-")
        val day = parts[2]
        val month = months[parts[1].toInt() - 1]
        val year = parts[0]
        return "$day $month $year"
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openMaps(lat: Double, lng: Double) {
        val uri = Uri.parse("http://maps.google.com/maps?q=loc:$lat,$lng")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val mapsPackageName = "com.google.android.apps.maps"

        if (isPackageExisted(mapsPackageName)) {
            intent.setClassName(mapsPackageName, "com.google.android.maps.MapsActivity")
            intent.setPackage(mapsPackageName)
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isPackageExisted(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun onDestroy() {
        pesananDetailViewModel.pesanans.removeObservers(this)
        pesananDetailViewModel.pesananSampah.removeObservers(this)
        super.onDestroy()
    }

    private fun setupRecyclerView() {
        this.adapter = ListDetailPesananAdapter(
            emptyList(),
            this,
            pesananDetailViewModel
        )
        binding.rvPesanan.layoutManager = LinearLayoutManager(this)
        binding.rvPesanan.adapter = adapter
    }

    private fun observeViewModel() {
        pesananDetailViewModel.pesananSampah.observe(this) { result ->
            if (result is Result.Success) {
                result.data.let { adapter.updatePesanan(it) }
            }
        }

        pesananDetailViewModel.changeStatus.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(this, "Status berhasil diubah", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Result.Error -> {
                    Toast.makeText(this, "Gagal mengubah status: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Toast.makeText(this, "Mengubah status...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}   