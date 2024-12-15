package com.example.saudemais

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

class Perfil : AppCompatActivity() {
    private  lateinit var tv: TextView
    private lateinit var tv1 :TextView
    private lateinit var tv2 :TextView
    private lateinit var tv3 :TextView
    private lateinit var tv4:TextView
    private lateinit var tv5 :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra("id")
        val logout = findViewById<Button>(R.id.logout)
        val nome = findViewById<Button>(R.id.nome)
        val email = findViewById<Button>(R.id.email)
        val pass = findViewById<Button>(R.id.pass)
        val dados = findViewById<Button>(R.id.dados)
        val doencas = findViewById<Button>(R.id.doencas)
        tv =findViewById<TextView>(R.id.user)
        tv1 = findViewById<TextView>(R.id.tvNome)
        tv2 = findViewById<TextView>(R.id.tvPeso)
        tv3 = findViewById<TextView>(R.id.tvAltura)
        tv4 = findViewById<TextView>(R.id.tvGenero)
        tv5 =  findViewById<TextView>(R.id.tvIdade)

        //chamar funcao para aparecer no inicio os dados do user
        getUser(id!!)
        nome.setOnClickListener() {
            button(id,tv)
        }
        email.setOnClickListener() {
            button2(id,tv)
        }
        pass.setOnClickListener() {
            button3(id,tv)
        }
        dados.setOnClickListener() {
            button4(id,tv)
        }
        doencas.setOnClickListener() {
            button5(id,tv)
        }

        //botao de logout com o limpar da stack de views
        logout.setOnClickListener() {
            val intent: Intent = Intent(this, Login::class.java,)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }
    /*
  funcao para ligar a API e  obter os dados de um utilizadro
  consoante o numero de id do utilizadro
  @params id utilizador
  se tiver sucesso entao aparece muda a textView para ter os dados do utilizador
  caso contrario erro
   */
    private fun getUser(id: String) {
        var nome:String? = null
        var peso:String? = null
        var altura:String? = null
        var dataNas:String? = null
        var genero:String? = null
        var alergias:String? = null
        var doencas:String? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .addHeader("id",id)
                    .url("http://nebula-env.com:8086/user")
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    val jsonArray = JSONArray(responseData)


                    for (i in 0 until jsonArray.length()) {

                        val jsonObject = jsonArray.getJSONObject(i)
                        nome = jsonObject.getString("nome")
                        Log.d("hgvyu",nome.toString())
                        peso = jsonObject.getString("peso")
                        altura = jsonObject.getString("altura")
                        dataNas = jsonObject.getString("dataNas")
                        genero = jsonObject.getString("genero")
                        alergias = jsonObject.getString("alergias")
                        doencas = jsonObject.getString("quadro")
                        //Log.d("hgvyu",peso.toString())
                        //Log.d("hgvyu",altura.toString())
                        //Log.d("hgvyu",genero.toString())
                        //Log.d("hgvyu",dataNas.toString())

                    }
                    //Log.d("teste",nome!!)
                   // Log.d("teste",responseData)

                    val textoMeio = alergias +"\n" + doencas

                    tv.text = textoMeio
                    tv1.text = nome
                    tv2.text = peso
                    tv3.text = altura
                    tv4.text = genero
                    tv5.text = dataNas

                } else {
                    Log.d("error", "Request failed: ${response.message}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("error", e.toString())
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
funcao para aparecer o popup para o utilizador mudar  o nome
@params id utilizador
é chamada a funcao changeNome com os inputs do utilizador
se tiver sucesso entao aparece um toast com uma mensagem de sucesso
caso contrario erro
*/
    private fun button(id: String,tv: TextView){
            //faz o dialog do nome da lista
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.dialog, null)
            val editTextDialog: EditText = dialogLayout.findViewById(R.id.dialog)
            with(builder) {
                setTitle("Novo Nome")
                setPositiveButton("Ok") { dialog, which ->
                    if (editTextDialog.text.toString().isNotEmpty()) {
                        changeNome(id, editTextDialog.text.toString(),tv)
                        Toast.makeText(context, "Nome Alterado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Add Nome ", Toast.LENGTH_SHORT).show()
                    }
                }
                setNegativeButton("Voltar") { dialog, _ ->
                    dialog.dismiss()
                }
                setView(dialogLayout)
                show()

            }
        }

    /*
funcao para aparecer o popup para o utilizador mudar  o email
@params id utilizador
é chamada a funcao changeEmail com os inputs do utilizador
se tiver sucesso entao aparece um toast com uma mensagem de sucesso
caso contrario erro
*/
    private fun button2(id: String,textView: TextView){
        //faz o dialog do nome da lista
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog, null)
        val editTextDialog: EditText = dialogLayout.findViewById(R.id.dialog)
        with(builder) {
            setTitle("Novo Email")
            setPositiveButton("Ok") { dialog, which ->
                if (editTextDialog.text.toString().isNotEmpty()) {
                    changeEmail(id, editTextDialog.text.toString(),textView)
                    Toast.makeText(context, "Email Alterado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Add email ", Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("Voltar") { dialog, _ ->
                dialog.dismiss()
            }
            setView(dialogLayout)
            show()

        }
    }
    /*
funcao para aparecer o popup para o utilizador mudar a password
@params id utilizador
é chamada a funcao changePass com os inputs do utilizador
se tiver sucesso entao aparece um toast com uma mensagem de sucesso
caso contrario erro
*/
    private fun button3(id: String,textView: TextView){
        //faz o dialog do nome da lista
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog3, null)
        val passL = dialogLayout.findViewById<TextInputLayout>(R.id.password)
        val pass = passL.editText
        val pass2L = dialogLayout.findViewById<TextInputLayout>(R.id.password2)
        val pass2 = pass2L.editText
        with(builder) {
            setTitle("Nova Password")
            setPositiveButton("Ok") { dialog, which ->
                if (pass?.text.toString().isNotEmpty()&& pass2?.text.toString().isNotEmpty()) {
                    if (!checkPass(pass?.text.toString())) {
                        passL.error = "nao cumpre requisitos"
                        pass2L.error = "nao cumpre requisitos"
                        Toast.makeText(context, "add password ", Toast.LENGTH_SHORT).show()
                        //Log.d("teste", "text: ${pass} ,error: ${pass2L.error} ,regex: ${pass.matches(regex)} ;tam: ${pass.length}")
                    } else {
                        changePass(id, pass!!.text.toString(),textView)
                        Toast.makeText(context, "Password Alterado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Add Password ", Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("Voltar") { dialog, _ ->
                dialog.dismiss()
            }
            setView(dialogLayout)
            show()

        }
    }
    /*
funcao para aparecer o popup para o utilizador mudar os dados
@params id utilizador
é chamada a funcao changeDados com os inputs do utilizador
se tiver sucesso entao aparece um toast com uma mensagem de sucesso
caso contrario erro
*/
    private fun button4(id: String,textView: TextView){
        //faz o dialog do nome da lista
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog2, null)

        val idadeL= dialogLayout.findViewById<TextInputLayout>(R.id.idade)
        val idade=  idadeL.editText
        val pesoL = dialogLayout.findViewById<TextInputLayout>(R.id.peso)
        val peso = pesoL.editText
        val alturaL= dialogLayout.findViewById<TextInputLayout>(R.id.altura)
        val altura = alturaL.editText
        val generoL= dialogLayout.findViewById<TextInputLayout>(R.id.genero)
        val genero = generoL.editText
        with(builder) {
            setTitle("Novos Dados")
            setPositiveButton("Ok") { dialog, which ->
                if (idade?.text.toString().isNotEmpty() && peso?.text.toString().isNotEmpty() && altura?.text.toString().isNotEmpty() &&genero?.text.toString().isNotEmpty()) {
                    changeDados(id, idade!!.text.toString(), peso!!.text.toString(), altura!!.text.toString(), genero!!.text.toString(),textView)
                    Toast.makeText(context, "Dados Alterados", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Add Dados ", Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("Voltar") { dialog, _ ->
                dialog.dismiss()
            }
            setView(dialogLayout)
            show()

        }
    }

    /*
funcao para aparecer o popup para o utilizador mudar as doencas
@params id utilizador
é chamada a funcao changeDoencas com os inputs do utilizador
se tiver sucesso entao aparece um toast com uma mensagem de sucesso
caso contrario erro
 */
    private fun button5(id: String,textView: TextView){
        //faz o dialog do nome da lista
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog4, null)
        val alergiasL = dialogLayout.findViewById<TextInputLayout>(R.id.alergias)
        val alergias = alergiasL.editText
        val doencasL = dialogLayout.findViewById<TextInputLayout>(R.id.doencas)
        val doencas = doencasL.editText
        with(builder) {
            setTitle("Novas Doencas")
            setPositiveButton("Ok") { dialog, which ->
                if (alergias?.text.toString().isNotEmpty() && doencas?.text.toString().isNotEmpty()) {
                    changeDoencas(id, alergias!!.text.toString(),doencas!!.text.toString(),textView)
                    Toast.makeText(context, "Doencas Alteradas", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Add Doencas ", Toast.LENGTH_SHORT).show()

                }
            }
            setNegativeButton("Voltar") { dialog, _ ->
                dialog.dismiss()
            }
            setView(dialogLayout)
            show()

        }
    }
    /*
funcao para ligar a API e  mudar  o nome de um utilizador
@params id utilizador
@params  nome string novo nome do utilizador
se tiver sucesso entao aparece um toast com uma mensagem de sucesso
caso contrario erro
 */
    private fun changeNome(id:String, nome:String,tv: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val json = "{\"id\": \"$id\",\"nome\": \"$nome\"}"
                Log.d("json", json)
                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .patch(requestBody)
                    .url("http://nebula-env.com:8086/alternome")
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    runOnUiThread() {
                        Toast.makeText(this@Perfil, "Nome Alterado", Toast.LENGTH_SHORT).show()
                       getUser(id )
                    }
                } else {
                    Log.d("error", "Request failed: ${response.message}")
                    Toast.makeText(this@Perfil,"Erro",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("error", e.toString())
            }
        }
    }

    /*
funcao para ligar a API e  mudar o email de um utilizador
@params id utilizador
@params  email string novo email
se tiver sucesso entao aparece um toast com uma mensagem de sucesso
caso contrario erro
*/
    private fun changeEmail(id:String, nome:String,tv: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val json = "{\"id\": \"$id\",\"email\": \"$nome\"}"
                Log.d("json", json)
                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .patch(requestBody)
                    .url("http://nebula-env.com:8086/alteremail")
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    Toast.makeText(this@Perfil,"Email Alterado",Toast.LENGTH_SHORT).show()
                    getUser(id,)
                } else {
                    Log.d("error", "Request failed: ${response.message}")
                    Toast.makeText(this@Perfil,"Erro",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("error", e.toString())
            }
        }
    }
    /*
funcao para ligar a API e  mudar a password de um utilizador
@params id utilizador
@params  password string nova password do utilzadro
se tiver sucesso entao aparece um toast com uma mensagem de sucesso
caso contrario erro
*/
    private fun changePass(id:String, pass:String,tv: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val json = "{\"id\": \"$id\",\"pass\": \"$pass\"}"
                Log.d("json", json)
                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .patch(requestBody)
                    .url("http://nebula-env.com:8086/alterpass")
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    Toast.makeText(this@Perfil,"Password Alterada",Toast.LENGTH_SHORT).show()
                    getUser( id!!)
                } else {
                    Log.d("error", "Request failed: ${response.message}")
                    Toast.makeText(this@Perfil,"Erro",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("error", e.toString())
            }
        }
    }
    /*
funcao para ligar a API e  mudar as dados de um utilizador
@params id utilizador
@params idade string de nova data de nascimento
@params  peso string com o novo peso do utilizador
@params  altura string com o nova altura do utilizador
@params  genero string com o novo genero do utilizador
se tiver sucesso entao aparece um toast com uma mensagem de sucesso
caso contrario erro
 */
    private fun changeDados(id: String, idade: String, peso: String, altura: String, genero: String,tv: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val json = "{\"id\": \"$id\",\"idade\": \"$idade\",\"peso\": \"$peso\",\"altura\": \"$altura\",\"genero\": \"$genero\"}"
                Log.d("json", json)
                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .patch(requestBody)
                    .url("http://nebula-env.com:8086/alterdados")
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    Toast.makeText(this@Perfil,"Dados Alterado",Toast.LENGTH_SHORT).show()
                    getUser(id)
                } else {
                    Log.d("error", "Request failed: ${response.message}")
                    Toast.makeText(this@Perfil,"Erro",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("error", e.toString())
            }
        }
    }
    /*
    funcao para ligar a API e  mudar as doencas de um utilizador
    @params id utilizador
    @params alergias string de novas alergias
    @params doencas string de novas doencas no quadro clinco
se tiver sucesso entao aparece um toast com uma mensagem de sucesso
caso contrario erro
     */
    private fun changeDoencas(id:String, alergias:String,doencas: String,tv: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val json = "{\"id\": \"$id\",\"alergias\": \"$alergias\",\"doencas\": \"$doencas\"}"
                Log.d("json", json)
                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                val request = Request.Builder()
                    .patch(requestBody)
                    .url("http://nebula-env.com:8086/alterdoencas")
                    .build()
                val response: Response = client.newCall(request).execute()
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    Toast.makeText(this@Perfil,"Doencas Alteradas",Toast.LENGTH_SHORT).show()
                    getUser(id)
                } else {
                    Log.d("error", "Request failed: ${response.message}")
                    Toast.makeText(this@Perfil,"Erro",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("error", e.toString())
            }
        }
    }

}