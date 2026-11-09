package com.kasirwoii.app

import android.content.Context
import okhttp3.*
import java.io.IOException

object ApiClient {
    private val client = OkHttpClient()

    private fun getBaseUrl(context: Context): String {
        val sharedPref = context.getSharedPreferences("KasirWoiiPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("URL_APPS_SCRIPT", "") ?: ""
    }

    fun getMenu(context: Context, callback: (String?) -> Unit) {
        val baseUrl = getBaseUrl(context)
        if (baseUrl.isEmpty()) { callback(null); return }

        val url = "$baseUrl?action=kasir_getMenu"
        client.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = callback(null)
            override fun onResponse(call: Call, response: Response) {
                response.use { callback(if (it.isSuccessful) it.body?.string() else null) }
            }
        })
    }

    fun kirimTransaksi(context: Context, total: Int, metode: String, callback: (Boolean) -> Unit) {
        val baseUrl = getBaseUrl(context)
        if (baseUrl.isEmpty()) { callback(false); return }

        val url = "$baseUrl?action=kasir_simpanTransaksi&total=$total&metode=$metode"
        client.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = callback(false)
            override fun onResponse(call: Call, response: Response) {
                response.use { callback(it.isSuccessful) }
            }
        })
    }

    fun getRiwayat(context: Context, callback: (String?) -> Unit) {
        val baseUrl = getBaseUrl(context)
        if (baseUrl.isEmpty()) { callback(null); return }

        val url = "$baseUrl?action=kasir_getRiwayat"
        client.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = callback(null)
            override fun onResponse(call: Call, response: Response) {
                response.use { callback(if (it.isSuccessful) it.body?.string() else null) }
            }
        })
    }
}
