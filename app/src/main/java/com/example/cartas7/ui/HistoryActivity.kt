package com.example.cartas7.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cartas7.data.AppDatabase
import com.example.cartas7.databinding.ActivityHistoryBinding
import java.util.concurrent.Executors

/**
 * Activity that displays all played games.
 */
class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    /**
     * Loads data in background and updates RecyclerView.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val dao = AppDatabase.getInstance(this).appDao()
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            val partidas = dao.getPartidasResumen()
            runOnUiThread {
                binding.recyclerView.adapter = PartidaAdapter(partidas)
            }
        }
    }
}
