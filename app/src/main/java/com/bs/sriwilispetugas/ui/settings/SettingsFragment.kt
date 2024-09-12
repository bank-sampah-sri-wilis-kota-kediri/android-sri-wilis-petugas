package com.bs.sriwilispetugas.ui.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bs.sriwilispetugas.databinding.FragmentSettingsBinding
import com.bs.sriwilispetugas.ui.authorization.LoginActivity
import com.bs.sriwilispetugas.ui.homepage.HomepageActivity
import com.bs.sriwilispetugas.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cvLogout.setOnClickListener {
                showLogoutDialog()
            }
        }
    }

    fun showLogoutDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Keluar Akun")
        builder.setMessage("Yakin ingin keluar dari akun?")

        builder.setPositiveButton("Ya") { dialogInterface, _ ->
            viewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("Tidak") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
