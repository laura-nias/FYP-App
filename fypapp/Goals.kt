package com.example.fypapp

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_goals.*

class Goals : AppCompatActivity(), CallbackListener {

    internal var dbhelper = GoalsDbHelper(this)
    var list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        bottom_nav.selectedItemId = R.id.goals

        val adapter =
            ArrayAdapter(this, R.layout.layout_checked_list, list)

        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter

        list(adapter)
        spin(adapter)
        suggestedGoals(adapter)

        floatingActionButton.setOnClickListener {
            val fullScreenDialog = Dialog(this, adapter)
            fullScreenDialog.isCancelable = true
            fullScreenDialog.show(supportFragmentManager, "MyTag")

        }

        if (adapter != listView) {
            adapter.notifyDataSetChanged()
        }


        delete.setOnClickListener {
            val position: SparseBooleanArray = listView.checkedItemPositions
            var items: Int = listView.count - 1

            while (items >= 0) {
                if (position.get(items)) {
                    val split = list[items].split("\n")
                    dbhelper.deleteTask(split[0])
                    adapter.remove(list[items])
                }
                items--
            }
            position.clear()
            adapter.notifyDataSetChanged()

            if (list.isEmpty()) {
                delete.isEnabled = false
                move.isEnabled = false
            }
        }

        move.setOnClickListener {
            val position: SparseBooleanArray = listView.checkedItemPositions
            var items: Int = listView.count - 1

            while (items >= 0) {
                if (position.get(items)) {
                    val split = list[items].split("\n")
                    dbhelper.setCompleted(split[0])
                    adapter.remove(list[items])
                }
                items--
            }
            position.clear()

            if (list.isEmpty()) {
                delete.isEnabled = false
                move.isEnabled = false
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

    fun list(adapter: ArrayAdapter<String>) {
        val results = dbhelper.getGoals

        list.clear()
        if (results.moveToFirst()) {
            while (!results.isAfterLast) {
                val data: String = results.getString(results.getColumnIndex("TASK"))
                val pri: String = results.getString(results.getColumnIndex("PRIORITY"))
                list.add(data + "\n\n" + pri)
                results.moveToNext()
            }
        } else {
            delete.isEnabled = false
            move.isEnabled = false
        }
        adapter.notifyDataSetChanged()
    }

    fun spin(adapter: ArrayAdapter<String>) {
        val spinner = findViewById<Spinner>(R.id.spinner)
        val ad = ArrayAdapter.createFromResource(
            this,
            R.array.sort_array,
            android.R.layout.simple_spinner_item
        )
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.prompt = "Filter List"

        spinner.adapter = NothingSelectedSpinnerAdapter(
            ad,
            R.layout.layout_nothing_selected_spinner,
            this
        )

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    if (selectedItem == "Show All") {
                        list(adapter)
                    } else {
                        list.clear()
                        val res = dbhelper.filterGoals(selectedItem)
                        if (res?.moveToFirst()!!) {
                            while (!res.isAfterLast) {
                                val data: String = res.getString(res.getColumnIndex("TASK"))
                                list.add(data + "\n\n" + selectedItem)
                                res.moveToNext()
                            }
                        }
                        ad.notifyDataSetChanged()
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    override fun onDataReceived(data: String, priority: String) {

        val t = dbhelper.checkDb(data)
        if (data != "" && t?.count == 0) {
            list.add(data + "\n\n" + priority)

            dbhelper.addTask(data, priority)

        } else {
            Toast.makeText(this, "Goal already exists", Toast.LENGTH_LONG).show()
        }
    }

    fun suggestedGoals(adapter: ArrayAdapter<String>) {
        val resdbhelper = ResultsDbHelper(this@Goals)

        val arr = ArrayList<String>()
        val catArr = ArrayList<String>()
        val res: Cursor? = resdbhelper.getResults()

        if (res?.moveToFirst()!!) {
            while (!res.isAfterLast) {
                val num: String = res.getString(res.getColumnIndex("NUM_SCORE"))
                val cat = res.getString(res.getColumnIndex("CATEGORY"))
                val id = res.getString(res.getColumnIndex("ID"))
                catArr.add(cat)

                if (num.toInt() <= 35) {
                    arr.add(cat)
                }
                res.moveToNext()
            }
        }

        var i = 0

        val load = resdbhelper.getLoaded()
        load?.moveToFirst()!!

        if (load.getString(load.getColumnIndex("LOADED")) == "0") {
            while (i < arr.size && arr[i].isNotEmpty()) {
                when (arr[i]) {
                    catArr[0] -> dbhelper.addTask(
                        "Suggested: Learn a how to use a new tool or skill.",
                        "High"
                    )
                    catArr[1] -> dbhelper.addTask(
                        "Suggested: Filter and file your online mailbox.",
                        "High"
                    )
                    catArr[2] -> dbhelper.addTask(
                        "Suggested: Try out some apps or services for collating information (e.g. Pinterest, Pearltrees).",
                        "High"
                    )
                    catArr[3] -> dbhelper.addTask(
                        "Suggested: Edit a digital image or video.",
                        "High"
                    )
                    catArr[4] -> dbhelper.addTask(
                        "Suggested: Practice creating charts in a spreadsheet program.",
                        "High"
                    )
                    catArr[5] -> dbhelper.addTask(
                        "Suggested: Engage in an online discussion (e.g. Twitter).",
                        "High"
                    )
                    catArr[6] -> dbhelper.addTask(
                        "Suggested: Widen the range of people I follow on social media and blogs.",
                        "High"
                    )
                    catArr[7] -> dbhelper.addTask(
                        "Suggested: Comment on other people’s blogs and maybe start my own.",
                        "High"
                    )
                    catArr[8] -> dbhelper.addTask(
                        "Suggested: Take an online course to learn a new creative software.",
                        "High"
                    )
                    catArr[9] -> dbhelper.addTask(
                        "Suggested: Try some lightweight research apps and tools.",
                        "High"
                    )
                    catArr[10] -> dbhelper.addTask(
                        "Suggested: Join in on a conversation about emerging technologies and future trends.",
                        "High"
                    )
                    catArr[11] -> dbhelper.addTask(
                        "Suggested: Ask other students what they use to support their studies and why.",
                        "High"
                    )
                    catArr[12] -> dbhelper.addTask(
                        "Suggested: Start a digital portfolio.",
                        "High"
                    )
                    catArr[13] -> dbhelper.addTask(
                        "Suggested: Link my sharing media with my social and professional media.",
                        "High"
                    )
                    catArr[14] -> dbhelper.addTask(
                        "Suggested: Help others to notice when their behaviour risks being misinterpreted or having negative consequences.",
                        "High"
                    )
                    catArr[15] -> dbhelper.addTask(
                        "Suggested: Practice taking online psychometric tests.",
                        "High"
                    )
                }

                i++

            }
            resdbhelper.setLoaded()
        }
        adapter.notifyDataSetChanged()
        listView.refreshDrawableState()
    }
}







