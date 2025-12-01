package com.outracoisa.n2do2alessandro

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.outracoisa.n2do2alessandro.databinding.ActivityMainBinding
import com.outracoisa.n2do2alessandro.helper.LolBlocker

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private var blockCheckRunnable: Runnable? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Check if user is blocked on startup
        if (LolBlocker.isUserBlocked(this)) {
            showBlockWarning()
        }
        
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        val bottomNav: BottomNavigationView = binding.bottomNavigation
        bottomNav.setupWithNavController(navController)
        
        // Start periodic block check
        startBlockCheck()
    }
    
    private fun showBlockWarning() {
        val timeRemaining = LolBlocker.getBlockTimeRemaining(this)
        val timeFormatted = LolBlocker.formatBlockTime(timeRemaining)
        
        AlertDialog.Builder(this)
            .setTitle("⚠️ Aviso Importante")
            .setMessage("Você está bloqueado por tentar adicionar League of Legends!\n\n" +
                    "Você pode visualizar as jogatinas existentes, mas não pode criar novas.\n\n" +
                    "Tempo restante: $timeFormatted\n\n" +
                    "Use esse tempo para considerar seus erros... 😤")
            .setPositiveButton("Entendi", null)
            .setCancelable(true)
            .show()
    }
    
    private fun startBlockCheck() {
        blockCheckRunnable = object : Runnable {
            override fun run() {
                if (LolBlocker.isUserBlocked(this@MainActivity)) {
                    // Check every minute while blocked
                    handler.postDelayed(this, 60000)
                } else {
                    // If was blocked and now unblocked, show celebration
                    val prefs = getSharedPreferences("JogatinasPrefs", MODE_PRIVATE)
                    if (prefs.contains("lol_block_until")) {
                        prefs.edit().remove("lol_block_until").apply()
                        Toast.makeText(
                            this@MainActivity,
                            "🎉 Você foi desbloqueado! Bem-vindo de volta!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        handler.post(blockCheckRunnable!!)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        blockCheckRunnable?.let { handler.removeCallbacks(it) }
    }
}