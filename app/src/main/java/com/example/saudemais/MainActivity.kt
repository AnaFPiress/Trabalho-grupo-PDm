package com.example.saudemais

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //obter o id e o nome que foram passados no
        // intent da view Login
        val id = intent.getStringExtra("id")
        val nome = intent.getStringExtra("name")
        val tv = findViewById<TextView>(R.id.Nome)
        val p = "Bem Vindo, $nome!"
        tv.text = p

        val button = findViewById<FloatingActionButton>(R.id.button6)
        val sintomas = findViewById<Button>(R.id.login)
        val mapa = findViewById<Button>(R.id.CriarConta)

        sintomas.setOnClickListener() {
            val intent: Intent = Intent(this, Sintomas::class.java)
            //passar o id no intent
            intent.putExtra("id",id)
            startActivity(intent)
        }

        mapa.setOnClickListener(){
            val gmmIntentUri = Uri.parse("geo:0,0?q=hospital")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        button.setOnClickListener(){
            val intent:Intent = Intent(this,Perfil::class.java)
            //passar o id no intent
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }
}