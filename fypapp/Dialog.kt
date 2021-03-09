package com.example.fypapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_goals.*
import kotlinx.android.synthetic.main.layout_dialog.*

class Dialog(private val callbackListener: CallbackListener, private val adapter : ArrayAdapter<String>) : DialogFragment()  {

    private val toolbar: Toolbar? = null
    var pri: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        return inflater.inflate(R.layout.layout_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        highPri.setOnClickListener {
            priText.text = ""
            pri = "High"
            priText.append("Priority: " + pri)
        }
        medPri.setOnClickListener {
            priText.text = ""
            pri = "Medium"
            priText.append("Priority: " + pri)
        }
        lowPri.setOnClickListener {
            priText.text = ""
            pri = "Low"
            priText.append("Priority: " + pri)
        }

        save.setOnClickListener {
            callbackListener.onDataReceived(goalText.text.toString(), pri)
            goalText.text.clear()
            pri = ""
           dismiss()
           adapter.notifyDataSetChanged()
        }

        cancel.setOnClickListener {
            goalText.text.clear()
            dismiss()
        }
        toolbar?.title = "New Goal"
        return

    }

    override fun onStart() {
        super.onStart()

        if(dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog!!.window?.setLayout(width, height)
        }
    }

}