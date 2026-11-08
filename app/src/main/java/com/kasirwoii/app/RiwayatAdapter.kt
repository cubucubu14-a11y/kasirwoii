package com.kasirwoii.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RiwayatAdapter(private val list: List<RiwayatItem>) :
    RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvId: TextView = view.findViewById(R.id.tvIdTrx)
        val tvTgl: TextView = view.findViewById(R.id.tvTglTrx)
        val tvMetode: TextView = view.findViewById(R.id.tvMetodeTrx)
        val tvNominal: TextView = view.findViewById(R.id.tvNominalTrx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvId.text = item.id
        holder.tvTgl.text = "${item.tanggal} ${item.jam}"
        holder.tvMetode.text = item.metode
        holder.tvNominal.text = "Rp ${String.format("%,d", item.nominal).replace(',', '.')}"
    }
    override fun getItemCount(): Int = list.size
}
