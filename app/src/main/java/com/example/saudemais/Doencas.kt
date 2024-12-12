package com.example.saudemais

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class Doencas : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doencas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button  = findViewById<Button>(R.id.button4)

        button.setOnClickListener(){
            if(ContextCompat.checkSelfPermission(this,CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                val call112 = Intent(Intent.ACTION_CALL)
                call112.setData(Uri.parse("tel:112"))
                startActivity(call112)
            }else{
              requestPermissions(arrayOf(CALL_PHONE),1)
            }
        }


    }
}