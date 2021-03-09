package com.example.fypapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_completed.*
import java.lang.Error
import java.text.SimpleDateFormat


class Completed: AppCompatActivity() {

    internal var dbhelper = GoalsDbHelper(this)
    var list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed)

        bottom_nav.selectedItemId = R.id.completed

        val ad = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)

        val listView: ListView = findViewById(R.id.completedView)
        listView.adapter = ad

        val results = dbhelper.getCompleted

        if (results.moveToFirst()) {
            noCompleted.visibility = View.INVISIBLE
            while (!results.isAfterLast) {
                val data: String = results.getString(results.getColumnIndex("TASK"))
                val date = results.getString(results.getColumnIndex("DATE"))
                list.add(data + "\n" + date)

                results.moveToNext()
            }
        }

        bottom_nav.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
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
                    true
                }
                else -> false
            }
        }
    }

}