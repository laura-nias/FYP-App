package com.example.fypapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat


class ExpandableListAdapter(var context: Context, var expandableListView : ExpandableListView, var header : MutableList<String>, var body : MutableList<MutableList<Any>>,  var progress : MutableList<MutableList<Int>>) : BaseExpandableListAdapter(){
    override fun getGroup(groupPosition: Int): String {
        return header[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView

        if(view == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.layout_group,null)
        }

        val title = view?.findViewById<TextView>(R.id.tv_title)
        title?.text = getGroup(groupPosition)

        title?.setOnClickListener {
            if(expandableListView.isGroupExpanded(groupPosition))
                expandableListView.collapseGroup(groupPosition)
            else
                expandableListView.expandGroup(groupPosition)
        }

        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return body[groupPosition].count()
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return body[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = convertView

        if(view == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.layout_child,null)
        }

        val title = view?.findViewById<TextView>(R.id.tv_title)
        title?.text = getChild(groupPosition,childPosition).toString().removeSurrounding("[", "]")

        setLayout(view, groupPosition, childPosition)
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return header.size
    }

    private fun setLayout(convertView: View?, groupPosition: Int, childPosition: Int) {
        val pB = convertView?.findViewById<ProgressBar>(R.id.progressBar1)
        pB?.progress = progress[groupPosition][childPosition]

        if(pB?.progress!! in 40..79){
            DrawableCompat.setTint(pB.progressDrawable!!, Color.rgb(252, 186, 3));
        }
        else if(pB.progress < 40){
            DrawableCompat.setTint(pB.progressDrawable!!, Color.rgb(255, 0, 0));
        }
        else{
            DrawableCompat.setTint(pB.progressDrawable!!, Color.rgb(52, 209, 4));
        }
    }

}



