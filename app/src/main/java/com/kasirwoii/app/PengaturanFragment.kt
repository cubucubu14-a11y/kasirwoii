package com.kasirwoii.app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class PengaturanFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pengaturan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etUrl = view.findViewById<EditText>(R.id.etUrlAppScript)
        val btnSimpan = view.findViewById<Button>(R.id.btnSimpanUrl)

        val sharedPref = requireContext().getSharedPreferences("KasirWoiiPrefs", Context.MODE_PRIVATE)
        etUrl.setText(sharedPref.getString("URL_APPS_SCRIPT", ""))

        btnSimpan.setOnClickListener {
            val urlBaru = etUrl.text.toString().trim()
            if (urlBaru.isEmpty()) Toast.makeText(context, "URL kosong", Toast.LENGTH_SHORT).show()
            else {
                sharedPref.edit().putString("URL_APPS_SCRIPT", urlBaru).apply()
                Toast.makeText(context, "URL Disimpan!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
