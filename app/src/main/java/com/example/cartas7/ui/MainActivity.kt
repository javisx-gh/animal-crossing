package com.example.cartas7.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cartas7.databinding.ActivityMainBinding

/**
 * Main menu activity.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * Initializes menu button listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNewGame.setOnClickListener {
            startActivity(Intent(this, RegisterGameActivity::class.java))
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.btnQuery.setOnClickListener {
            startActivity(Intent(this, PlayerQueryActivity::class.java))
        }
    }
}
