package com.bs.sriwilispetugas.ui.history

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
import com.bs.sriwilispetugas.R
import com.bs.sriwilispetugas.adapter.HistoryPesananAdapter
import com.bs.sriwilispetugas.adapter.PesananAdapter
import com.bs.sriwilispetugas.databinding.FragmentHistoryBinding
import com.bs.sriwilispetugas.databinding.FragmentHomeBinding
import com.bs.sriwilispetugas.helper.Result
import com.bs.sriwilispetugas.ui.homepage.HomePageViewModel
import com.bs.sriwilispetugas.utils.ViewModelFactory
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryPesananAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

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
        adapter = HistoryPesananAdapter(
            emptyList(),
            requireContext(),
            object : HistoryPesananAdapter.OnApproveClickListener {
                override fun onApproveClick(idPesanan: String) {
                    //
                }

            }
        )
        binding.rvPesananSelesai.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPesananSelesai.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.historyPesanans.observe(viewLifecycleOwner) { result ->
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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}