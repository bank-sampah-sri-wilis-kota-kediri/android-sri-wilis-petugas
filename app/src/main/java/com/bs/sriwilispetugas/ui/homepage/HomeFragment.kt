package com.bs.sriwilispetugas.ui.homepage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bs.sriwilispetugas.adapter.PesananAdapter
import com.bs.sriwilispetugas.databinding.FragmentHomeBinding
import com.bs.sriwilispetugas.helper.Result
import com.bs.sriwilispetugas.utils.ViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomePageViewModel
    private lateinit var adapter: PesananAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[HomePageViewModel::class.java]

        setupRecyclerView()
        lifecycleScope.launch {
            viewModel.getCombinedPesananData()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.syncData()
                viewModel.getCombinedPesananData()
            }
        }
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = PesananAdapter(
            emptyList(),
            requireContext(),
            object : PesananAdapter.OnApproveClickListener {
                override fun onApproveClick(idPesanan: String, status: String) {
                    viewModel.changeStatusPesananSampahResponse(idPesanan, status)
                }
            }
        )
        binding.rvPesanan.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPesanan.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.pesanans.observe(viewLifecycleOwner) { result ->
            binding.swipeRefreshLayout.isRefreshing = false
            when (result) {
                is Result.Success -> {
                    result.data.let { adapter.updatePesanan(it) }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Gagal memuat data: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Log.d("Loading", "Loading")
                }
            }
        }

        viewModel.changeStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    lifecycleScope.launch {
                        viewModel.syncData()
                        Toast.makeText(requireContext(), "Status berhasil diubah", Toast.LENGTH_SHORT).show()
                        viewModel.getCombinedPesananData()
                    }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Gagal mengubah status: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Toast.makeText(requireContext(), "Mengubah status...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}