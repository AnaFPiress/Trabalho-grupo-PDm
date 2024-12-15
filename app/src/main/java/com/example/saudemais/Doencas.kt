package com.example.saudemais

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray


class Doencas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doencas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val id = intent.getStringExtra("id")
        val text = intent.getStringExtra("sintomas")
        val texto = findViewById<TextView>(R.id.textView10)
        val tv12 = findViewById<EditText>(R.id.textView12)
        val tv = findViewById<TextView>(R.id.doenca)
        //val tv2 = findViewById<TextView>(R.id.desc)
        val tv1 = findViewById<TextView>(R.id.trat)
        //Log.d("Doencas",text.toString())
        val button  = findViewById<Button>(R.id.button4)
        Log.d("da",text.toString())
        tv.text = text
        val a = text!!.split("\n")
        val a2 = id!!.split("\n")
        println(a2.size)

        val array = mutableListOf<String>()
        for (i in a2.indices) {
            getTrat(a2[i],array, tv1,a2.size)
        }
        button.setOnClickListener(){
            if(ContextCompat.checkSelfPermission(this,CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                val call112 = Intent(Intent.ACTION_DIAL)
                call112.setData(Uri.parse("tel:112"))
                startActivity(call112)
            }else{
              requestPermissions(arrayOf(CALL_PHONE),1)
            }
        }
        tv12.setOnClickListener(){
            val intent:Intent = Intent(this,teste::class.java)
            startActivity(intent)
        }

    }


    private fun getTrat(id: String,array: MutableList<String>, tv:TextView,size:Int){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                //val json = "{\"sintomas\": \"$sintomas\"}"
                val json = id.toString()
                val encodedJson = java.net.URLEncoder.encode(json, "UTF-8")
                Log.d("json", json)
                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .addHeader("id",id)
                    .url("http://nebula-env.com:8086/trat")
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    val jsonArray = JSONArray(responseData)
                    val diseasesList = mutableListOf<String>()
                    val diseasesList2 = mutableListOf<String>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        diseasesList.add(jsonObject.getString("descricaoT"))
                        //diseasesList2.add(jsonObject.getString("descricao"))
                    }
                    val diseasesText = diseasesList.joinToString(separator = "\n")
                    //val diseasesText2 = diseasesList2[0]
                    Log.d("doencas", diseasesText)
                    tv.text = diseasesText

                    //tv1.text = diseasesText2
                } else {
                    Log.d("error", "Request failed: ${response.message}")
                }

            } catch (e:Exception){
                e.printStackTrace()
                Log.d("error", e.toString())
            }
        }
    }
}