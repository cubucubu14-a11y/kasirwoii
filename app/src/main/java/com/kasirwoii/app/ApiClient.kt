package com.kasirwoii.app

import okhttp3.*
import java.io.IOException

object ApiClient {
    // ⚠️ MASUKKAN URL WEB APP APPS SCRIPT KAMU DI SINI ⚠️
    private const val BASE_URL = "URL_APPS_SCRIPT_KAMU" 
    private val client = OkHttpClient()

    // 1. Ambil Data Menu dari Spreadsheet
    fun getMenu(callback: (String?) -> Unit) {
        val url = "$BASE_URL?action=kasir_getMenu"
        client.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = callback(null)
            override fun onResponse(call: Call, response: Response) {
                response.use { callback(if (it.isSuccessful) it.body?.string() else null) }
            }
        })
    }

    // 2. Kirim Transaksi Baru ke Spreadsheet
    fun kirimTransaksi(total: Int, metode: String, callback: (Boolean) -> Unit) {
        val url = "$BASE_URL?action=kasir_simpanTransaksi&total=$total&metode=$metode"
        client.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = callback(false)
            override fun onResponse(call: Call, response: Response) {
                response.use { callback(it.isSuccessful) }
            }
        })
    }

    // 3. Ambil Riwayat Transaksi (Untuk Halaman Penjualan)
    fun getRiwayat(callback: (String?) -> Unit) {
        val url = "$BASE_URL?action=kasir_getRiwayat"
        client.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = callback(null)
            override fun onResponse(call: Call, response: Response) {
                response.use { callback(if (it.isSuccessful) it.body?.string() else null) }
            }
        })
    }
}
