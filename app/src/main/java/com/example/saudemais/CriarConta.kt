package com.example.saudemais

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray

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
                        val nomeF = nome!!.text.toString().plus(" ").plus(nome2!!.text.toString())
                        showCustomAlertDialog(nomeF,pass!!.text.toString(),email!!.text.toString(),idade!!.text.toString(),peso!!.text.toString().toFloat(),altura!!.text.toString().toFloat(),genero!!.text.toString(),alergias!!.text.toString(),doencas!!.text.toString())

                    }
                }
            }
        }
    }

    // Metodo para criar conta
    fun createAccount(username: String, password: String, email: String,idade:String, peso :Float, altura :Float,genero: String,alergias:String, doencas:String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val json = JSONArray(arrayOf(username, password, email, peso, altura, idade, genero, alergias, doencas)).toString()

                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .post(requestBody)
                    .url("http://nebula-env.com:8086/register")
                    .build()

                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    Log.d("success", "Account created: $responseData")
                    //textView.text = "Conta criada com sucesso!"
                    finish()
                } else {
                    Log.d("error", "Account creation failed: ${response.message}")
                    Toast.makeText(this@CriarConta,"erro!",Toast.LENGTH_SHORT).show()
                    //textView.text = "Erro ao criar conta: ${response.message}"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("error", e.toString())
                Toast.makeText(this@CriarConta,"erro!",Toast.LENGTH_SHORT).show()
                //textView.text = "Erro ao criar conta: ${e.message}"
            }
        }
    }

    /*
funcao para verificar se a password obdece a algumas regras
tais como:
8 letras minimo
pelo menos uma 1 maiuscula, 1 minuscula, 1 numero e um
dos seguintes simbolos @#$%^&+= e sem espacos brancos
@params pass string com a password
@returns Boolean true se sim, falso se nao
*/
    private fun checkPass(pass: String): Boolean {
        val regex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%^&+=])(?=\\S+\$).{8,}\$")
        //Log.d("teste", "olha aqyu")
        //Log.d("teste2", "text: ${pass} ,regex: ${pass.matches(regex)} ;tam: ${pass.length}")
        return pass.matches(regex)

    }

    /*
funcao para verificar se o tamanho da string inroduzida no editText é
maior q o permitido no Layout
@params genero: EditText string introduzida pelo utilizador
@params generoL: TextInputLayout Layout com o numero de carateres posiveis
caso seja maiorr coloca o erro no layout, caso contrario o texto é  null
*/
    private fun checkTam(genero: EditText, generoL: TextInputLayout) {
        if (genero.text.toString().length > generoL.counterMaxLength) {
            //Log.d("teste", "testedslknfisd")
            generoL.error = "muitas letras"
        } else {
            generoL.error = null
        }
    }


    private fun showCustomAlertDialog(nomeF: String,pass:String,email:String,idade:String,peso:Float,altura:Float,genero:String,alergias:String,doencas:String) {
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
            createAccount(nomeF,pass,email,idade,peso,altura,genero,alergias,doencas)
            finish()
        }

        // botao discordar
        val button1 = customView.findViewById<Button>(R.id.Disc)
        button1.setOnClickListener {
           Toast.makeText(this,"Tem de aceitar",Toast.LENGTH_SHORT).show()
        }

    }
}