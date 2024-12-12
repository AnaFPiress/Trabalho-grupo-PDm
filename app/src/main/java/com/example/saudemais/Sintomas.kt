package com.example.saudemais

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout

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

        val sint1L = findViewById<TextInputLayout>(R.id.sintoma1)
        val sint1 = sint1L.editText
        val sint2L = findViewById<TextInputLayout>(R.id.sintoma2)
        val sint2 = sint2L.editText
        val sint3L = findViewById<TextInputLayout>(R.id.sintoma3)
        val sint3 = sint3L.editText
        val button = findViewById<Button>(R.id.button3)
        button.setOnClickListener(){
            if (sint1?.text.toString().isEmpty()) {
                Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
            } else if (sint2?.text.toString().isEmpty()){
                Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
            }else if(sint3?.text.toString().isEmpty()) {
                Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
            }else  {
                if (sint1!!.text.toString().length > sint1L.counterMaxLength){
                    checkTam(sint1,sint1L)
                }else if (sint2!!.text.toString().length > sint2L.counterMaxLength){
                    checkTam(sint2,sint2L)
                }else if(sint3!!.text.toString().length > sint3L.counterMaxLength){
                    checkTam(sint3,sint3L)
                }else {

                    val intent: Intent = Intent(this, Doencas::class.java)
                    startActivity(intent)

                }
            }

        }

    }

    private fun checkTam(genero: EditText, generoL: TextInputLayout) {
        if (genero.text.toString().length > generoL.counterMaxLength) {
            //Log.d("teste", "testedslknfisd")
            generoL.error = "muitas letras"
        } else {
            generoL.error = null
        }
    }
}