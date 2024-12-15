package com.example.saudemais

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray

class Sintomas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sintomas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id  = intent.getStringExtra("id")
        val tv9 = findViewById<TextView>(R.id.textView9)
        val tv3 = findViewById<TextView>(R.id.textView3)
        val sint1L = findViewById<TextInputLayout>(R.id.sintoma1)
        val sint1 = sint1L.editText
        val sint2L = findViewById<TextInputLayout>(R.id.sintoma2)
        val sint2 = sint2L.editText
        val sint3L = findViewById<TextInputLayout>(R.id.sintoma3)
        val sint3 = sint3L.editText
        val button = findViewById<Button>(R.id.button3)
        val auto = findViewById<AutoCompleteTextView>(R.id.autoComplete)
        val auto2 = findViewById<AutoCompleteTextView>(R.id.autoComplete1)
        val auto3 = findViewById<AutoCompleteTextView>(R.id.autoComplete2)
        val arrayAdapter: ArrayAdapter<String>
        val sintomasArray = resources.getStringArray(R.array.sintomas)
        arrayAdapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,sintomasArray)
        auto.setAdapter(arrayAdapter)
        auto2.setAdapter(arrayAdapter)
        auto3.setAdapter(arrayAdapter)


        button.setOnClickListener {
            if (sint1?.text.toString().isEmpty() && sint2?.text.toString().isEmpty() && sint3?.text.toString().isEmpty()){
                Toast.makeText(this, "Pelo menos 1 Sintoma", Toast.LENGTH_SHORT).show()
            }else  {
                if (sint1!!.text.toString().length > sint1L.counterMaxLength){
                    checkTam(sint1,sint1L)
                }else if (sint2!!.text.toString().length > sint2L.counterMaxLength){
                    checkTam(sint2,sint2L)
                }else if(sint3!!.text.toString().length > sint3L.counterMaxLength){
                    checkTam(sint3,sint3L)
                }else {
                    val intent: Intent = Intent(this, Doencas::class.java)
                    if (sint1.text.toString().isEmpty()) sint1.setText("")
                    if (sint2.text.toString().isEmpty()) sint2.setText("")
                    if (sint3.text.toString().isEmpty()) sint3.setText("")
                    postSintomas(sint1.text.toString(),sint2.text.toString(),sint3.text.toString(),id!!,intent)
                }
            }

        }

    }

    /*
    recebe a edit text e o Layout para verificar o tamanho introduzido
    caso seja maior aparece o erro no Layout
     */
    private fun checkTam(genero: EditText, generoL: TextInputLayout) {
        if (genero.text.toString().length > generoL.counterMaxLength) {
            //Log.d("teste", "testedslknfisd")
            generoL.error = "muitas letras"
        } else {
            generoL.error = null
        }
    }



    /*
Função para enviar os sintomas para a API e exibir as doenças retornadas
Lança uma corrotina para realizar o POST usando OkHttp.
*/
    private fun postSintomas(sintomas: String,sintomas1: String,sintomas2: String, id:String, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                //val json = "{\"sintomas\": \"$sintomas\"}"
                //Log.d("json", json)
                //val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    //.post(requestBody)
                    .addHeader("sint1",sintomas)
                    .addHeader("sint2",sintomas1)
                    .addHeader("sint3",sintomas2)
                    .addHeader("id",id)
                    .url("http://nebula-env.com:8086/queryDoenca")
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    val jsonArray = JSONArray(responseData)
                    val diseasesList = mutableListOf<String>()
                    val diseasesList2 = mutableListOf<String>()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        diseasesList2.add(jsonObject.getString("id_doenca"))
                        diseasesList.add(jsonObject.getString("name"))
                    }
                    val diseasesText2 = diseasesList2.joinToString(separator = "\n")
                    val diseasesText = diseasesList.joinToString(separator = "\n")
                    Log.d("doencas", diseasesText)
                    intent.putExtra("id",diseasesText2)
                    intent.putExtra("sintomas",diseasesText)
                    startActivity(intent)
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