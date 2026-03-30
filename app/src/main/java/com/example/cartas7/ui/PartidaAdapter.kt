package com.example.cartas7.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cartas7.data.PartidaResumen
import com.example.cartas7.databinding.ItemPartidaBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Recycler adapter for game summary rows.
 */
class PartidaAdapter(private val items: List<PartidaResumen>) :
    RecyclerView.Adapter<PartidaAdapter.PartidaViewHolder>() {

    /**
     * View holder wrapper for one row binding.
     */
    class PartidaViewHolder(val binding: ItemPartidaBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates one row.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidaViewHolder {
        val binding = ItemPartidaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PartidaViewHolder(binding)
    }

    /**
     * Binds one game summary into row views.
     */
    override fun onBindViewHolder(holder: PartidaViewHolder, position: Int) {
        val item = items[position]
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.binding.tvDate.text = formatter.format(Date(item.fechaMillis))
        holder.binding.tvWinner.text = item.ganador
    }

    /**
     * Returns number of rows.
     */
    override fun getItemCount(): Int = items.size
}
