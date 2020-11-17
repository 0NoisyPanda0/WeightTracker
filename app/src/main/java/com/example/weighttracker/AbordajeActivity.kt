package com.example.weighttracker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_abordaje.*
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_home.*

class AbordajeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abordaje)

        //setup
        val bundle = intent.extras
        val email = bundle?.getString("email")

        Setup(email?: "")

    }

    private fun Setup(email: String){
        continueButton.setOnClickListener {
            val restante : Int = pesoActualEditText.text.toString().toInt() - pesoFuturoEditText.text.toString().toInt()

            if (pesoActualEditText.text.isNotEmpty() && pesoFuturoEditText.text.isNotEmpty()){
                db.collection("users").document(email).set(
                    hashMapOf("original" to pesoActualEditText.text.toString(),
                        "objetivo" to restante.toString(),
                        "restante" to pesoFuturoEditText.text.toString())
                )

                showHome(email?:"",pesoActualEditText.text.toString()?:"",restante.toString()?:"",pesoFuturoEditText.text.toString()?:"")
            }
            else{
                showAlert()
            }
        }
    }

    private fun showHome(email: String, actual: String, objetivo: String, restante: String){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email",email)
            putExtra("actual", actual)
            putExtra("objetivo", objetivo)
            putExtra("restante", restante)
        }
        startActivity(homeIntent)
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autenticación")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}