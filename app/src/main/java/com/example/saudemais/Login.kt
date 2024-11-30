package com.example.saudemais

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)

        button.setOnClickListener() {
            val intent: Intent = Intent(this, MainActivity::class.java)
        }

        button2.setOnClickListener() {
            val intent: Intent = Intent(this, CriarConta::class.java)
            startActivity(intent)
        }
        if (!email.text.isNullOrEmpty()){
            startActivity(Intent(this, MainActivity::class.java))
        }

        if (!password.text.isNullOrEmpty()){
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

}