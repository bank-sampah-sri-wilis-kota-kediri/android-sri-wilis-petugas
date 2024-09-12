package com.bs.sriwilispetugas.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.databinding.CardPesananDetailBinding
import com.bs.sriwilispetugas.ui.homepage.PesananDetailViewModel

class ListDetailPesananAdapter(
    private var pesananSampah: List<CardDetailPesanan>,
    private val context: Context,
    private val viewModel: PesananDetailViewModel
) : RecyclerView.Adapter<ListDetailPesananAdapter.HomePageViewHolder>() {

    inner class HomePageViewHolder(private val binding: CardPesananDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(pesananSampah: CardDetailPesanan) {
            with(binding) {
                tvKategoriPesanan.text = pesananSampah.nama_kategori
                tvBeratPesanan.text = "${pesananSampah.berat} Kg"

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDetailPesananAdapter.HomePageViewHolder {
        val binding = CardPesananDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePageViewHolder(binding)
    }

    override fun getItemCount(): Int{
        return pesananSampah.size
    }

    override fun onBindViewHolder(holder: ListDetailPesananAdapter.HomePageViewHolder, position: Int){
        holder.bind(pesananSampah[position])
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updatePesanan(newPesananSampah: List<CardDetailPesanan>) {
        this.pesananSampah = newPesananSampah
        notifyDataSetChanged()
    }

}