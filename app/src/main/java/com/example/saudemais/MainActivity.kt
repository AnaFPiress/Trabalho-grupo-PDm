package com.example.saudemais


import com.example.saudemais.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity :
    AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Exibe o alerta na tela
        //showCustomAlertDialog()

        val login: Button = findViewById(R.id.login)
        val criarConta = findViewById<Button>(R.id.CriarConta)
        login.setOnClickListener() {
            val intent: Intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        criarConta.setOnClickListener(){
            val intent : Intent = Intent(this, CriarConta::class.java)
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