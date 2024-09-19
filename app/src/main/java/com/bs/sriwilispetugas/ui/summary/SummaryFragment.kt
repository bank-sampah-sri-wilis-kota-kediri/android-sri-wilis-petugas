package com.bs.sriwilispetugas.ui.summary

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bs.sriwilispetugas.R
import com.bs.sriwilispetugas.databinding.FragmentSettingsBinding
import com.bs.sriwilispetugas.databinding.FragmentSummaryBinding
import com.bs.sriwilispetugas.helper.Result
import com.bs.sriwilispetugas.ui.authorization.LoginActivity
import com.bs.sriwilispetugas.ui.homepage.HomePageViewModel
import com.bs.sriwilispetugas.ui.settings.SettingViewModel
import com.bs.sriwilispetugas.utils.ViewModelFactory
import kotlinx.coroutines.launch

class SummaryFragment : Fragment() {
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomePageViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)


        lifecycleScope.launch {
            viewModel.getCombinedPesananData()
        }

        observeViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    private fun observeViewModel() {
        viewModel.pesanans.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val data = result.data

                    var jumlahCount = data.size
                    var perluDiantarCount = data.count { it.status_pesanan.lowercase() == "sudah dijadwalkan" }
                    var terkonfirmasiCount = data.count { it.status_pesanan.lowercase() == "selesai diantar" || it.status_pesanan.lowercase() == "gagal" }
                    var selesaiDiantarCount = data.count { it.status_pesanan.lowercase() == "selesai diantar" }
                    var gagalCount = data.count { it.status_pesanan.lowercase() == "gagal" }

                    binding.tvValueJumlah.text = jumlahCount.toString()
                    binding.tvValuePerluDiantar.text = perluDiantarCount.toString()
                    binding.tvValueTerkonfirmasi.text = terkonfirmasiCount.toString()
                    binding.tvValueSelesai.text = selesaiDiantarCount.toString()
                    binding.tvValueGagal.text = gagalCount.toString()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Gagal memuat data: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Log.d("Loading", "Loading")
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}