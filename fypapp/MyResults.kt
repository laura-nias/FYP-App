package com.example.fypapp

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_myresults.*
import kotlinx.android.synthetic.main.activity_myresults.bottom_nav

class MyResults : AppCompatActivity() {

    internal var dbhelper = ResultsDbHelper(this)

    val catList: MutableList<String> = ArrayList()
    val resList: MutableList<MutableList<Any>> = ArrayList()
    val progress: MutableList<MutableList<Int>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myresults)

        bottom_nav.selectedItemId = R.id.myResults

        val resHeaders: Cursor? = dbhelper.getHeaders()
        val results: Cursor? = dbhelper.getResults()
        var count = 0

        if (resHeaders != null && results != null) {
            if (resHeaders.moveToFirst() && results.moveToFirst()) {
                while (!resHeaders.isAfterLast) {

                    while (!results.isAfterLast) {
                        resList.add(arrayListOf())
                        progress.add(arrayListOf())
                        val header: String =
                            resHeaders.getString(resHeaders.getColumnIndexOrThrow("HEADER"))

                        val res: String =
                            results.getString(results.getColumnIndexOrThrow("RESULT"))

                        val cat: String =
                            results.getString(results.getColumnIndexOrThrow("CATEGORY"))

                        val resHead: String =
                            results.getString(results.getColumnIndexOrThrow("HEADER"))

                        val prog: Int =
                            results.getInt(results.getColumnIndexOrThrow("NUM_SCORE"))

                        if (resHead == header) {
                            resList[count].add(arrayListOf(cat + ": " + res))
                            progress[count].add(prog)
                            results.moveToNext()

                            if(results.isLast){
                                catList.add(header)
                            }
                        } else {
                            resHeaders.moveToNext()
                            catList.add(header)
                            count += 1
                        }
                    }
                    break
                }
            }
        }
        expandableListView.setAdapter(ExpandableListAdapter(this, expandableListView, catList, resList, progress))

        bottom_nav.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.myResults -> {
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