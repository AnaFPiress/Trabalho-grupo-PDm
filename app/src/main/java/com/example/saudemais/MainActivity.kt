package com.example.saudemais

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val button: Button = findViewById(R.id.button)

        button.setOnClickListener() {
            val intent: Intent = Intent(this, Sintomas::class.java)
            startActivity(intent)
        }
    }
}