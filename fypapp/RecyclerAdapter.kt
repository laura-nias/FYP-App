package com.example.fypapp

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.fypapp.RecyclerAdapter.ViewHolder


class RecyclerAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val titles = arrayOf("The Skills Centre", "LinkedIn Learning", "Microsoft", "Sheffield Digital Skills", "Codecademy", "Digital Wings", "Skills for Tomorrow", "Lloyds Bank Academy", "Google Digital Garage")

    private val details = arrayOf("Sheffield Hallam's skill development platform", "Online courses, training and tutorials ", "Digital skills courses", "Find out what is happening in and around Sheffield", "Learn how to code with online courses", "Tools and tutorials to help boost your digital knowledge", "Empowering you with the skills you need today, for a better tomorrow", "Free digital skills learning for any level", "Online courses designed for you to grow")

    private val images = intArrayOf(R.drawable.shu_logo, R.drawable.linkedin_logo, R.drawable.microsoft_logo, R.drawable.sheffield_digital_skills_logo, R.drawable.codecademy_logo, R.drawable.digitalwings_logo, R.drawable.bt_logo,R.drawable.lloydsacademy_logo,R.drawable.googledigitalgarage_logo)

    private val link = arrayOf("https://blogs.shu.ac.uk/skillscentre/other-skill-development-opportunities/?doing_wp_cron=1581890352.4618349075317382812500", "https://www.linkedin.com/learning/", "https://www.microsoft.com/en-gb/athome/digitalskills/improve/" ,"https://www.sheffielddigitalskills.org.uk", "https://www.codecademy.com", "https://digital.wings.uk.barclays/for-everyone", "https://www.bt.com/skillsfortomorrow/", "https://www.lloydsbankacademy.co.uk/#", "https://learndigital.withgoogle.com/digitalgarage")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImage: ImageView = itemView.findViewById(R.id.item_image)
        var itemTitle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_detail)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cards_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitle.text = titles[i]
        viewHolder.itemDetail.text = details[i]
        viewHolder.itemImage.setImageResource(images[i])

        viewHolder.itemView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link[i]))
            viewHolder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return titles.size
    }
}


