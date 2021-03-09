package com.example.fypapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottom_nav
import kotlinx.android.synthetic.main.activity_nextsteps.*


class NextSteps : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nextsteps)

        bottom_nav.selectedItemId = R.id.nextSteps

        layoutManager = LinearLayoutManager(this)
        recView.layoutManager = layoutManager

        adapter = RecyclerAdapter()
        recView.adapter = adapter

        bottom_nav.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId){
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.myResults -> {
                    startActivity(Intent(this, MyResults::class.java))
                    true
                }
                R.id.nextSteps -> {
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