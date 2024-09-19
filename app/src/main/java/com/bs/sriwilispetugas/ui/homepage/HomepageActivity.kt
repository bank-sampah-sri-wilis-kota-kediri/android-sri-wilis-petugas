package com.bs.sriwilispetugas.ui.homepage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bs.sriwilispetugas.R
import com.bs.sriwilispetugas.databinding.ActivityHomepageBinding
import com.bs.sriwilispetugas.ui.history.HistoryFragment
import com.bs.sriwilispetugas.ui.settings.SettingsFragment
import com.bs.sriwilispetugas.ui.summary.SummaryFragment

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = true

        replaceFragment(SummaryFragment())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    if (!isCurrentFragment(SummaryFragment::class.java)) {
                        replaceFragment(SummaryFragment())
                    }
                    true
                }
                R.id.pesanan -> {
                    if (!isCurrentFragment(HomeFragment::class.java)) {
                        replaceFragment(HomeFragment())
                    }
                    true
                }
                R.id.riwayat -> {
                    if (!isCurrentFragment(HistoryFragment::class.java)) {
                        replaceFragment(HistoryFragment())
                    }
                    true
                }
                R.id.settings -> {
                    if (!isCurrentFragment(SettingsFragment::class.java)) {
                        replaceFragment(SettingsFragment())
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun isCurrentFragment(fragmentClass: Class<out Fragment>): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        return currentFragment != null && currentFragment::class.java == fragmentClass
    }
}