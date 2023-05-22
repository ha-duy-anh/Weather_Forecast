package com.example.weatherforecast.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R

class WeekWeatherRVAdapter(var data: List<WeekWeather>,
                           private val listener:  (WeekWeather) -> Unit) : RecyclerView.Adapter<WeekWeatherRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : WeekWeatherRVAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.week_rview, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: WeekWeatherRVAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val imgView1: ImageView = v.findViewById(R.id.dailyWeather1)
        private val txtView2: TextView = v.findViewById(R.id.dailyWeather2)
        private val txtView3: TextView = v.findViewById(R.id.dailyWeather3)
        private val txtView4: TextView = v.findViewById(R.id.dailyWeather4)

        fun bind(item: WeekWeather) {
            val imgRes = AppCompatResources.getDrawable(v.context, item.weatherImg)
            imgView1.setImageDrawable(imgRes)
            txtView2.text = item.dayList
            txtView3.text = item.descList
            txtView4.text = item.tempList

            v.setOnClickListener { listener(item) }
        }
    }
}