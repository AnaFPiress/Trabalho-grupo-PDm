package com.example.saudemais

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class CriarConta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_criar_conta)

        findViewById<TextView>(R.id.textView3)

        val nomeL = findViewById<TextInputLayout>(R.id.nome)
        val nome = nomeL.editText
        val nome2L = findViewById<TextInputLayout>(R.id.nome2)
        val nome2 = nome2L.editText
        val emailL = findViewById<TextInputLayout>(R.id.email)
        val email = emailL.editText
        val passL = findViewById<TextInputLayout>(R.id.password)
        val pass = passL.editText
        val pass2L = findViewById<TextInputLayout>(R.id.password2)
        val pass2 = pass2L.editText
        val idadeL = findViewById<TextInputLayout>(R.id.idade)
        val idade = idadeL.editText
        val pesoL = findViewById<TextInputLayout>(R.id.peso)
        val peso = pesoL.editText
        val alturaL = findViewById<TextInputLayout>(R.id.altura)
        val altura = alturaL.editText
        val generoL = findViewById<TextInputLayout>(R.id.genero)
        val genero = generoL.editText
        val alergiasL = findViewById<TextInputLayout>(R.id.alergias)
        val alergias = alergiasL.editText
        val doencasL = findViewById<TextInputLayout>(R.id.doencas)
        val doencas = doencasL.editText

        val button = findViewById<Button>(R.id.CriarConta)

        var cont = 0
        button.setOnClickListener {
            if (cont == 0) {
                if (nome?.text.toString().isEmpty()) {
                    Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
                } else if (nome2?.text.toString().isEmpty()) {
                    Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
                } else if (email?.text.toString().isEmpty()) {
                    Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
                } else if (pass?.text.toString().isEmpty() || pass2?.text.toString().isEmpty() || (pass?.text.toString() != pass2?.text.toString())) {
                    Toast.makeText(this, "passwords diferents", Toast.LENGTH_SHORT).show()
                } else {
                    if (!checkPass(pass?.text.toString())) {
                        passL.error = "nao cumpre requisitos"
                        pass2L.error = "nao cumpre requisitos"
                        //Log.d("teste", "text: ${pass} ,error: ${pass2L.error} ,regex: ${pass.matches(regex)} ;tam: ${pass.length}")
                    } else {
                        Toast.makeText(this, "fkhbds", Toast.LENGTH_SHORT).show()

                        nomeL.visibility = View.INVISIBLE
                        nome?.visibility = View.INVISIBLE
                        nome2L.visibility = View.INVISIBLE
                        nome2?.visibility = View.INVISIBLE
                        emailL.visibility = View.INVISIBLE
                        email?.visibility = View.INVISIBLE
                        pass?.visibility = View.INVISIBLE
                        pass2?.visibility = View.INVISIBLE
                        passL.visibility = View.INVISIBLE
                        pass2L.visibility = View.INVISIBLE

                        idadeL.visibility = View.VISIBLE
                        idade?.visibility = View.VISIBLE
                        pesoL.visibility = View.VISIBLE
                        peso?.visibility = View.VISIBLE
                        alturaL.visibility = View.VISIBLE
                        altura?.visibility = View.VISIBLE
                        generoL.visibility = View.VISIBLE
                        genero?.visibility = View.VISIBLE
                        alergiasL.visibility = View.VISIBLE
                        alergias?.visibility = View.VISIBLE
                        doencasL.visibility = View.VISIBLE
                        doencas?.visibility = View.VISIBLE

                        cont += 1
                        button.text = "Criar Conta"
                        //println(cont)
                    }
                }
            } else if (cont != 0) {
                //Log.d("teste", nome?.text.toString())
                if (idade?.text.toString().isEmpty()) {
                    Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
                } else if (peso?.text.toString().isEmpty()) {
                    Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
                } else if (altura?.text.toString().isEmpty()) {
                    Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
                } else if (genero?.text.toString().isEmpty()) {
                    Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
                } else if (alergias?.text.toString().isEmpty()) {
                    Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
                } else if (doencas?.text.toString().isEmpty()) {
                    Toast.makeText(this, "falta input", Toast.LENGTH_SHORT).show()
                } else {
                    //println(genero?.text.toString().length)
                    if (genero?.text.toString().length > generoL.counterMaxLength) {
                        checkTam(genero!!, generoL)
                    } else if (alergias?.text.toString().length > alergiasL.counterMaxLength) {
                        checkTam(alergias!!, alergiasL)
                    } else if (doencas?.text.toString().length > doencasL.counterMaxLength) {
                        checkTam(doencas!!, doencasL)
                    } else {
                        //cont += 1
                        Toast.makeText(this, "fkhbds!!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_SHORT)
                            .show()
                        //println(cont)
                        // funcao aqui para adicoinar a db
                        finish()
                    }
                }
            }
        }
    }

    private fun checkPass(pass: String): Boolean {
        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+\$).{8,}\$")
        //Log.d("teste", "olha aqyu")
        //Log.d("teste2", "text: ${pass} ,regex: ${pass.matches(regex)} ;tam: ${pass.length}")
        return pass.matches(regex)

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