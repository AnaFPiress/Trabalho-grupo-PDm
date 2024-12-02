package com.example.saudemais

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray

class apresentacaoPDM : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_apresentacao_pdm)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = findViewById<Button>(R.id.button5)
        val editText = findViewById<EditText>(R.id.editTextText)
        val textView = findViewById<TextView>(R.id.textView)

        button.setOnClickListener {
            val text = editText.text.toString()
            postDB(text,textView)
        }
    }


    /*
    funcao que recebe uma textView e faz um get a api
    lanca uma coroutina e faz um request
    utilizando okhttp client com o url http://nebula-env.com:8086/query
    executa o request e pega o dados returnados no body
    converte num JSONArray e muda o texto da textView para o ultimo na tabela
     */
    private fun getDB(text:TextView)  {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://nebula-env.com:8086/query")
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    val jsonArray = JSONArray(responseData)
                    for (i in 0 until  jsonArray.length()) {
                        //println(jsonArray.length())
                        if (i == jsonArray.length() -1) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            println(jsonObject)
                            text.text = jsonObject.getString("texto")
                        }else{
                            continue
                        }
                    }
                } else {
                    Log.d("error", "Request failed: ${response.message}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("error", e.toString())
            }
        }
    }

    /*
    funcao que recebe uma string e uma textView e faz um post a api
    lanca uma coroutina e faz um request utilizando okhttp client
    com o url http://nebula-env.com:8086/post
    manda o texto recebido no body em formato json
    chama a funcao getDB para atualizar a textView
     */
    private fun postDB(texto: String, textView: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val json = """{"texto": "$texto"}"""
                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .post(requestBody)
                    .url("http://nebula-env.com:8086/post")
                    .build()
                val response: Response = client.newCall(request).execute()
                //val responseData = response.body?.string()

                if (response.isSuccessful) {
                    Log.d("success", "Request successful: ${response.message}")
                    getDB(textView)
                } else {
                    Log.d("error", "Request failed: ${response.message}")

                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("error", e.toString())
            }
        }
    }


}