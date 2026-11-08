package com.kasirwoii.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class PenjualanFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_penjualan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val rv = view.findViewById<RecyclerView>(R.id.rvRiwayat)
        val progress = view.findViewById<ProgressBar>(R.id.progressBarRiwayat)
        
        rv.layoutManager = LinearLayoutManager(context)
        progress.visibility = View.VISIBLE

        ApiClient.getRiwayat { json ->
            activity?.runOnUiThread {
                progress.visibility = View.GONE
                if (json != null) {
                    try {
                        val res = Gson().fromJson(json, RiwayatResponse::class.java)
                        rv.adapter = RiwayatAdapter(res.data)
                    } catch (e: Exception) { Toast.makeText(context, "Gagal baca riwayat", Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }
}

data class RiwayatResponse(val status: String, val data: List<RiwayatItem>)
data class RiwayatItem(
    val id: String, val tanggal: String, val jam: String,
    val nominal: Int, val metode: String
)
