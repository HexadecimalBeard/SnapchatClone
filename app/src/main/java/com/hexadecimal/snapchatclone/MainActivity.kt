package com.hexadecimal.snapchatclone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        // giris yapmis kullanici olup olmadigini kontrol ediyoruz

        if ( mAuth.currentUser != null) {
            // eger kullanici giris yapmamis ise mAuth.currentUser null degerini alacak
            logIn()

        }

    }

    fun goClicked ( view : View) {
        // kullanici go tusuna tikladiginda, once kullanicinin giris yapip yapamayacagini kontrol et

        // emailEditText ve passwordEditText nullable olduklari icin degisken adlarinin sonuna soru isareti "?" ekledik
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //eger kullanici giris yapabiliyorsa giris yap
                    logIn()
                } else {
                    // giris yapamiyorsa sisteme kaydet
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString()).addOnCompleteListener(this) { task ->
                        // yukarida kullandigimiz task isimli nesne bize islemin sonucunu dondurur
                        if (task.isSuccessful) {
                            // asagidaki kodla firebase'in database kismina eristik
                            // ikinci child icinde task kullandik, bu task giris yapmaya calisilan kullanici emailini ve sifresini barindirir
                            FirebaseDatabase.getInstance().getReference().child("users").child(task.result!!.user.uid).child("email").setValue(emailEditText?.text.toString())
                            logIn()
                        } else {
                            // eger kayit yapilamazsa
                            Toast.makeText(this, "Log in failed. Try again!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

    }

    fun logIn() {
        // sonraki activity'e gec
        // kotlin'de intent ile baska bir sinifa gecerken, gecis yapilacak sinifi asagidaki gibi belirtiyoruz
        val intent = Intent (this, SnapsActivity::class.java)
        startActivity(intent)
    }
}
