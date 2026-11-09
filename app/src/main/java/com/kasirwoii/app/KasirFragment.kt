package com.kasirwoii.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

data class ItemKeranjang(val menu: MenuItem, val qty: Int, val catatan: String)

class KasirFragment : Fragment() {
    private val listKeranjang = mutableListOf<ItemKeranjang>()
    private var totalTagihan = 0
    private lateinit var tvCartCount: TextView
    private lateinit var tvCartTotal: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_kasir, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewMenu)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val btnBayar = view.findViewById<Button>(R.id.btnBayar)
        tvCartCount = view.findViewById(R.id.tvCartCount)
        tvCartTotal = view.findViewById(R.id.tvCartTotal)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Cek apakah URL sudah disetel di Pengaturan
        val sharedPref = requireContext().getSharedPreferences("KasirWoiiPrefs", android.content.Context.MODE_PRIVATE)
        val urlTersimpan = sharedPref.getString("URL_APPS_SCRIPT", "") ?: ""

        if (urlTersimpan.isEmpty()) {
            Toast.makeText(context, "Harap isi URL Apps Script di menu Pengaturan!", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
        } else {
            // Ambil Menu dari Spreadsheet (Kirim requireContext())
            progressBar.visibility = View.VISIBLE
            ApiClient.getMenu(requireContext()) { jsonResponse ->
                activity?.runOnUiThread {
                    progressBar.visibility = View.GONE
                    if (jsonResponse != null) {
                        try {
                            val responseData = Gson().fromJson(jsonResponse, MenuResponse::class.java)
                            recyclerView.adapter = MenuAdapter(responseData.data) { menu -> tampilkanBottomSheet(menu) }
                        } catch (e: Exception) { 
                            Toast.makeText(context, "Gagal baca data menu", Toast.LENGTH_SHORT).show() 
                        }
                    } else { 
                        Toast.makeText(context, "Gagal koneksi ke Internet", Toast.LENGTH_SHORT).show() 
                    }
                }
            }
        }

        btnBayar.setOnClickListener {
            if (listKeranjang.isEmpty()) Toast.makeText(context, "Keranjang kosong", Toast.LENGTH_SHORT).show()
            else tampilkanLayarPembayaran()
        }
    }

    private fun tampilkanBottomSheet(menu: MenuItem) {
        val dialog = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
        dialog.setContentView(v)

        v.findViewById<TextView>(R.id.tvNamaItem).text = menu.nama
        v.findViewById<TextView>(R.id.tvHargaItem).text = "Rp ${String.format("%,d", menu.harga).replace(',', '.')}"
        val tvQty = v.findViewById<TextView>(R.id.tvQty)
        
        var qty = 1
        v.findViewById<Button>(R.id.btnMinus).setOnClickListener { if (qty > 1) { qty--; tvQty.text = qty.toString() } }
        v.findViewById<Button>(R.id.btnPlus).setOnClickListener { qty++; tvQty.text = qty.toString() }

        v.findViewById<Button>(R.id.btnTambahKeranjang).setOnClickListener {
            val catatan = v.findViewById<EditText>(R.id.etCatatan).text.toString()
            listKeranjang.add(ItemKeranjang(menu, qty, catatan))
            updateKeranjangUI()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun updateKeranjangUI() {
        totalTagihan = 0
        var totalItem = 0
        for (item in listKeranjang) { 
            totalTagihan += item.menu.harga * item.qty
            totalItem += item.qty 
        }
        tvCartCount.text = "$totalItem Item"
        tvCartTotal.text = "Rp ${String.format("%,d", totalTagihan).replace(',', '.')}"
    }

    private fun tampilkanLayarPembayaran() {
        val dialog = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.layout_pembayaran, null)
        dialog.setContentView(v)
        dialog.behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED

        val rv = v.findViewById<RecyclerView>(R.id.rvKeranjang)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = KeranjangAdapter(listKeranjang)
        
        v.findViewById<TextView>(R.id.tvTotalTagihan).text = "Total: Rp ${String.format("%,d", totalTagihan).replace(',', '.')}"

        var metode = "Tunai"
        val btnTunai = v.findViewById<Button>(R.id.btnTunai)
        val btnQris = v.findViewById<Button>(R.id.btnQris)

        btnTunai.setOnClickListener { 
            metode = "Tunai"
            btnTunai.setBackgroundColor(android.graphics.Color.parseColor("#2563EB"))
            btnQris.setBackgroundColor(android.graphics.Color.parseColor("#94A3B8"))
        }
        btnQris.setOnClickListener { 
            metode = "QRIS"
            btnQris.setBackgroundColor(android.graphics.Color.parseColor("#2563EB"))
            btnTunai.setBackgroundColor(android.graphics.Color.parseColor("#94A3B8"))
        }

        val btnKonfirmasi = v.findViewById<Button>(R.id.btnKonfirmasiCetak)
        btnKonfirmasi.setOnClickListener {
            btnKonfirmasi.isEnabled = false
            btnKonfirmasi.text = "Menyimpan..."
            
            // Kirim ke Spreadsheet (Kirim requireContext())
            ApiClient.kirimTransaksi(requireContext(), totalTagihan, metode) { sukses ->
                activity?.runOnUiThread {
                    if (sukses) {
                        dialog.dismiss()
                        tampilkanStruk(totalTagihan, metode)
                        listKeranjang.clear()
                        updateKeranjangUI()
                    } else {
                        btnKonfirmasi.isEnabled = true
                        btnKonfirmasi.text = "Konfirmasi & Cetak"
                        Toast.makeText(context, "Gagal simpan ke database", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        dialog.show()
    }

    private fun tampilkanStruk(total: Int, metode: String) {
        val dialog = BottomSheetDialog(requireContext())
        val v = layoutInflater.inflate(R.layout.layout_struk, null)
        dialog.setContentView(v)
        dialog.setCancelable(false)

        v.findViewById<TextView>(R.id.tvWaktuStruk).text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        
        var detail = ""
        for (item in listKeranjang) { 
            detail += "${item.qty}x ${item.menu.nama}"
            if(item.catatan.isNotEmpty()) detail += " (${item.catatan})"
            detail += "\n" 
        }
        v.findViewById<TextView>(R.id.tvDetailStruk).text = detail

        v.findViewById<TextView>(R.id.tvTotalStruk).text = "Total: Rp ${String.format("%,d", total).replace(',', '.')}"
        v.findViewById<TextView>(R.id.tvMetodeStruk).text = "Metode: $metode"
        
        v.findViewById<Button>(R.id.btnTutupStruk).setOnClickListener { 
            dialog.dismiss() 
        }
        dialog.show()
    }
}

data class MenuResponse(val status: String, val data: List<MenuItem>)
data class MenuItem(
    val id: String,
    val nama: String,
    val harga: Int,
    val kategori: String,
    val tersedia: Boolean
)
