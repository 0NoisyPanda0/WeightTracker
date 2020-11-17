package com.example.weighttracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_abordaje.*
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType {
    BASIC
}

class HomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        val actual = bundle?.getString("actual")
        val objetivo = bundle?.getString("objetivo")
        val restante = bundle?.getString("restante")
        Setup(email?: "",provider?: "",actual?: "", objetivo?: "", restante?: "")

        //Guardar datos
        val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()
    }

    private fun Setup(email: String, provider: String, actual: String, objetivo: String, restante: String){
        title = "Pesos(lbs)"

        emailTextView.text = email
        providerTextView.text = provider
        OriginalWtextView.text= actual
        ObjetiveWtextView.text = objetivo
        ResWtextView.setText(restante)

        logOutbutton.setOnClickListener {
            //Borrar datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        saveButton.setOnClickListener {
            db.collection("users").document(email).set(
                hashMapOf("original" to OriginalWtextView.text.toString(),
                "objetivo" to ObjetiveWtextView.text.toString(),
                "restante" to ResWtextView.text.toString())
            )
        }

        getButton.setOnClickListener {
                db.collection("users").document(email).get().addOnSuccessListener {
                    OriginalWtextView.text = it.get("original") as String?
                    ObjetiveWtextView.text = it.get("objetivo") as String?
                    ResWtextView.setText(it.get("restante") as String?)
            }
        }

    }
}