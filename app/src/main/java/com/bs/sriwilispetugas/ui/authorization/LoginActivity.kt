package com.bs.sriwilispetugas.ui.authorization

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bs.sriwilispetugas.R
import com.bs.sriwilispetugas.databinding.ActivityLoginBinding
import com.bs.sriwilispetugas.ui.homepage.HomepageActivity
import com.bs.sriwilispetugas.utils.ViewModelFactory
import com.bs.sriwilispetugas.helper.Result
import com.bs.sriwilispetugas.ui.splashscreen.WelcomeActivity
import com.bs.sriwilispetugas.ui.splashscreen.WelcomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private var isPasswordVisible = false
    private lateinit var binding: ActivityLoginBinding

    private val welcomeViewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory.getInstance(WelcomeActivity())
    }


    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPasswordToggle()
        observeViewModel()
        setupAction()
    }

    private fun login(phone: String, password: String) {
        viewModel.login(phone, password)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPasswordToggle() {
        binding.edtEmailPasswordForm.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.edtEmailPasswordForm.right - binding.edtEmailPasswordForm.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    val selection = binding.edtEmailPasswordForm.selectionEnd
                    if (isPasswordVisible) {
                        binding.edtEmailPasswordForm.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.edtEmailPasswordForm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_close, 0)
                    } else {
                        binding.edtEmailPasswordForm.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.edtEmailPasswordForm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_open, 0)
                    }
                    binding.edtEmailPasswordForm.setSelection(selection)
                    isPasswordVisible = !isPasswordVisible
                    return@OnTouchListener true
                }
            }
            false
        })
    }


    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val phone = binding.edtEmailLoginForm.text.toString()
            val password = binding.edtEmailPasswordForm.text.toString()

            login(phone, password)
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    lifecycleScope.launch {
                        welcomeViewModel.syncData()
                    }
                    AlertDialog.Builder(this).apply {
                        setTitle("Sukses")
                        setMessage("Anda Berhasil Masuk")
                        setPositiveButton("OK") { _, _ ->
                            val intent = Intent(this@LoginActivity, HomepageActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Gagal!")
                        setMessage("Email atau sandi yang dimasukkan salah")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        })
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }
}