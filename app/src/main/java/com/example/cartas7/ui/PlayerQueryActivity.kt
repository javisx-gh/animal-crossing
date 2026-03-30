package com.example.cartas7.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cartas7.R
import com.example.cartas7.data.AppDatabase
import com.example.cartas7.databinding.ActivityPlayerQueryBinding
import java.util.concurrent.Executors

/**
 * Activity used to search games for one player.
 */
class PlayerQueryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerQueryBinding

    /**
     * Handles query action and renders filtered games.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerQueryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewQuery.layoutManager = LinearLayoutManager(this)
        val dao = AppDatabase.getInstance(this).appDao()
        val executor = Executors.newSingleThreadExecutor()

        binding.btnSearch.setOnClickListener {
            val name = binding.etQuery.text.toString().trim()
            executor.execute {
                val partidas = dao.getPartidasByJugador(name)
                runOnUiThread {
                    binding.tvTotal.text = getString(R.string.label_total_games, partidas.size)
                    binding.recyclerViewQuery.adapter = PartidaAdapter(partidas)
                }
            }
        }
    }
}
