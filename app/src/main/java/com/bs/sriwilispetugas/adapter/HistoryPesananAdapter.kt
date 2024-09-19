package com.bs.sriwilispetugas.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilispetugas.R
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan
import com.bs.sriwilispetugas.databinding.CardPesananBinding
import com.bs.sriwilispetugas.databinding.CardPesananSelesaiBinding
import com.bs.sriwilispetugas.ui.homepage.PesananDetailActivity
import com.bs.sriwilispetugas.ui.homepage.HomePageViewModel
import java.util.Locale


class HistoryPesananAdapter(
    private var pesanans: List<CardPesanan>,
    private val context: Context,
    private val listener: OnApproveClickListener
) : RecyclerView.Adapter<HistoryPesananAdapter.HistoryViewHolder>() {


    interface OnApproveClickListener {
        fun onApproveClick(idPesanan: String)
    }

    inner class HistoryViewHolder(private val binding: CardPesananSelesaiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pesanan: CardPesanan) {
            with(binding) {
                tvNomorWaPesanan.text = pesanan.no_hp_nasabah
                tvTanggalPesanan.text = convertDateToText(pesanan.tanggal)
                val totalBerat = pesanan.total_berat
                tvBeratTransaksi.text = totalBerat.toString()

                tvNamaPesanan.text = pesanan.nama_nasabah ?: "Nama tidak ditemukan"

                if(pesanan.status_pesanan.lowercase() == "selesai diantar"){
                    tvStatus.text = "Selesai"
                    tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.green_label))
                }else{
                    tvStatus.text = "Gagal"
                    tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.red_primary))
                }

                root.setOnClickListener {
                    val intent = Intent(context, PesananDetailActivity::class.java)
                    intent.putExtra("ID_PESANAN", pesanan.id_pesanan)
                    context.startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openMaps(lat: Double, lng: Double) {
        val uri = Uri.parse("http://maps.google.com/maps?q=loc:$lat,$lng")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Google Maps is not installed.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryPesananAdapter.HistoryViewHolder {
        val binding = CardPesananSelesaiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int{
        return pesanans.size
    }

    override fun onBindViewHolder(holder: HistoryPesananAdapter.HistoryViewHolder, position: Int){
        holder.bind(pesanans[position])
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

    @SuppressLint("NotifyDataSetChanged")
    fun updatePesanan(newPesanans: List<CardPesanan>) {
        this.pesanans = newPesanans
        notifyDataSetChanged()
    }



}
