package com.example.saudemais


import android.content.Context
import com.example.saudemais.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity :
    AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Exibe o alerta na tela
        //showCustomAlertDialog()
        val id = intent.getStringExtra("id")
        val nome = intent.getStringExtra("name")
        //Log.d("dasd",nome.toString())
        val tv = findViewById<TextView>(R.id.Nome)
        val p :String = "Bem Vindo, $nome!"
        tv.text = p
        val button = findViewById<FloatingActionButton>(R.id.button6)
        //val auto = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
        //val arrayAdapter: ArrayAdapter<String>
        //val sintomasArray = resources.getStringArray(R.array.sintomas)
        //arrayAdapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,sintomasArray)
        //auto.setAdapter(arrayAdapter)



        val sintomas: Button = findViewById(R.id.login)
        val criarConta = findViewById<Button>(R.id.CriarConta)
        sintomas.setOnClickListener() {
            val intent: Intent = Intent(this, Sintomas::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }

        criarConta.setOnClickListener(){
            val intent : Intent = Intent(this, teste::class.java)
            startActivity(intent)
        }
        button.setOnClickListener(){
            val intent:Intent = Intent(this,Perfil::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }

    private fun showCustomAlertDialog() {
        // criação do alerta
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)

        val customView = layoutInflater.inflate(R.layout.popup, null)
        builder.setView(customView)

        val alert = builder.create()
        alert.show()


        val button = customView.findViewById<Button>(R.id.Conc)
        button.setOnClickListener {
            alert.dismiss()
        }

        // botao discordar
        val button1 = customView.findViewById<Button>(R.id.Disc)
        button1.setOnClickListener {
            val intent: Intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

    }
}