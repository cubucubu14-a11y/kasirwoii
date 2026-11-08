package com.kasirwoii.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(
    private val listMenu: List<MenuItem>,
    private val onClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmoji: TextView = view.findViewById(R.id.tvEmoji)
        val tvNama: TextView = view.findViewById(R.id.tvNamaMenu)
        val tvHarga: TextView = view.findViewById(R.id.tvHargaMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = listMenu[position]
        holder.tvEmoji.text = menu.emoji
        holder.tvNama.text = menu.nama
        holder.tvHarga.text = "Rp ${String.format("%,d", menu.harga).replace(',', '.')}"
        
        holder.itemView.setOnClickListener {
            onClick(menu)
        }
    }

    override fun getItemCount(): Int = listMenu.size
}
