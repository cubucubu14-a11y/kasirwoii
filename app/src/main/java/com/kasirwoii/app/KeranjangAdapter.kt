package com.kasirwoii.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KeranjangAdapter(private val list: List<ItemKeranjang>) :
    RecyclerView.Adapter<KeranjangAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaItem)
        val tvCatatan: TextView = view.findViewById(R.id.tvCatatanItem)
        val tvHarga: TextView = view.findViewById(R.id.tvHargaItem)
        val tvQty: TextView = view.findViewById(R.id.tvQtyItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvNama.text = item.menu.nama
        holder.tvHarga.text = "Rp ${String.format("%,d", item.menu.harga * item.qty).replace(',', '.')}"
        holder.tvQty.text = "x${item.qty}"
        
        if (item.catatan.isNotEmpty()) {
            holder.tvCatatan.visibility = View.VISIBLE
            holder.tvCatatan.text = item.catatan
        } else {
            holder.tvCatatan.visibility = View.GONE
        }
    }
    override fun getItemCount(): Int = list.size
}
