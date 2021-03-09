package com.example.fypapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_signup.*

class SignUp : AppCompatActivity() {
    internal var dbhelper = LoginDbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        submitBtn.setOnClickListener {

            val uName = findViewById<TextInputLayout>(R.id.username).editText?.text.toString()
            val email = findViewById<TextInputLayout>(R.id.email).editText?.text.toString()
            val pass = findViewById<TextInputLayout>(R.id.pass).editText?.text.toString()

            dbhelper.signUp(uName, email, pass)
            val intent = Intent(this, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        back.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

}