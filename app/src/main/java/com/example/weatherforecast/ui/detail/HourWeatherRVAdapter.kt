package com.example.weatherforecast.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R

class HourWeatherRVAdapter(var data:List<HourWeather>) : RecyclerView.Adapter<HourWeatherRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.hour_rview, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val imgView1: ImageView = v.findViewById(R.id.dayWeather)
        private val txtView2: TextView = v.findViewById(R.id.dayTemp)
        private val txtView3: TextView = v.findViewById(R.id.dayTime)

        fun bind(item: HourWeather) {
            val imgRes = AppCompatResources.getDrawable(v.context, item.weatherImg)
            imgView1.setImageDrawable(imgRes)
            txtView2.text = item.temp
            txtView3.text = item.time
        }
    }
}