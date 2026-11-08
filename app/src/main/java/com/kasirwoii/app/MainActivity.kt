package com.kasirwoii.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMenu)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        
        val dummyMenu = listOf(
            MenuItem("🍔", "Burger Spesial", 25000),
            MenuItem("🌭", "Hotdog Bakar", 20000),
            MenuItem("🍟", "Kentang Goreng", 15000),
            MenuItem("🧊", "Es Teh Manis", 5000)
        )
        
        recyclerView.adapter = MenuAdapter(dummyMenu) { menu ->
            // Aksi saat menu diklik
        }
    }
}

data class MenuItem(
    val emoji: String,
    val nama: String,
    val harga: Int
)
