package com.example.fypapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        val filename = "loginInfo.txt"
//        val fileContent = applicationContext.assets.open(filename).bufferedReader().use{
//            it.readText()
//        }
//

        //welcome.text = "Welcome " + fileContent.lines()[0] + "!"

        logout.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        bottom_nav.setOnNavigationItemSelectedListener {
                item: MenuItem -> return@setOnNavigationItemSelectedListener when (item.itemId){
                R.id.home -> {
                    true
                }
                R.id.myResults -> {
                    startActivity(Intent(this, MyResults::class.java))
                    true
                }
                R.id.nextSteps -> {
                    startActivity(Intent(this, NextSteps::class.java))
                    true
                }
                R.id.goals -> {
                    startActivity(Intent(this, Goals::class.java))
                    true
                }
                R.id.completed -> {
                    startActivity(Intent(this, Completed::class.java))
                    true
                }

                else -> false
            }
        }

    }


}
