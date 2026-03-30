package com.example.cartas7.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartas7.R
import com.example.cartas7.data.AppDatabase
import com.example.cartas7.databinding.ActivityRegisterGameBinding
import com.example.cartas7.logic.GameEngine
import java.util.concurrent.Executors

/**
 * Activity used to register and simulate one game.
 */
class RegisterGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterGameBinding

    /**
     * Creates listeners and starts game simulation in background thread.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val executor = Executors.newSingleThreadExecutor()
        val dao = AppDatabase.getInstance(this).appDao()
        val engine = GameEngine(dao)

        binding.btnPlay.setOnClickListener {
            val names = listOf(
                binding.etPlayer1.text.toString().trim(),
                binding.etPlayer2.text.toString().trim(),
                binding.etPlayer3.text.toString().trim()
            )
            if (names.any { it.isBlank() }) {
                Toast.makeText(this, getString(R.string.validation_names), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.tvResult.text = getString(R.string.status_playing)
            executor.execute {
                val result = engine.playGame(names)
                runOnUiThread {
                    binding.tvResult.text = if (result.message == "OK") {
                        getString(R.string.result_winner, result.winner ?: "-")
                    } else {
                        getString(R.string.result_error)
                    }
                }
            }
        }
    }
}
