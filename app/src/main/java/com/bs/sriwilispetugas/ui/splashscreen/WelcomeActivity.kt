package com.bs.sriwilispetugas.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bs.sriwilispetugas.R
import com.bs.sriwilispetugas.ui.homepage.HomepageActivity
import com.bs.sriwilispetugas.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.log
import com.bs.sriwilispetugas.helper.Result

class WelcomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        lifecycleScope.launch {
            checkToken()
        }
    }

    private fun navigateToSplash() {
        val intent = Intent(this, SplashScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
        finish()
    }

    private suspend fun checkToken() {
        val token = viewModel.fetchToken()

        if (token != null) {
            when(val result = viewModel.syncData()){
                is Result.Loading -> {
                    Log.d("Sync Data", "Loading...")
                }
                is Result.Success -> {
                    navigateToHome()
                }
                is Result.Error -> {
                    Toast.makeText(this, result.error ?: "Error occurred", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }
            }
        } else {
            navigateToSplash()
        }
    }
}