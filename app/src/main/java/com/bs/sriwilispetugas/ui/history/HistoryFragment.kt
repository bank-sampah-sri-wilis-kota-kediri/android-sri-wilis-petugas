package com.bs.sriwilispetugas.ui.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
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

@Suppress("DEPRECATION")
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryPesananAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        viewModel.filterData("semua")

        observeViewModel()

        binding.menuIcon.setOnClickListener {
            showPopupMenu(binding.menuIcon)
        }

        return binding.root
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_filter_history, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.filter_all -> {
                    viewModel.filterData("semua")
                    true
                }
                R.id.filter_completed -> {
                    viewModel.filterData("selesai diantar")
                    true
                }
                R.id.filter_failed -> {
                    viewModel.filterData("gagal")
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
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