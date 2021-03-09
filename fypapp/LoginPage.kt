package com.example.fypapp

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_loginpage.*
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class LoginPage : AppCompatActivity() {

    internal var dbhelper = LoginDbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginpage)

        Login()
    }

    fun Login() {

        loginBtn.setOnClickListener {
            val username =
                findViewById<TextInputLayout>(R.id.editUsername).editText?.text.toString()
            val pass = findViewById<TextInputLayout>(R.id.editPassword).editText?.text.toString()

            if(username != "" || pass != "") {
                val test = dbhelper.checkInfo(username, pass)

                if (test) {

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Wrong credentials", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty field(s)", Toast.LENGTH_SHORT).show()
            }
        }

        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

    }
}
