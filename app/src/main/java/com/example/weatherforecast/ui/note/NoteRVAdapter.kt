package com.example.weatherforecast.ui.note

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherforecast.R

class NoteRVAdapter(var data: List<Note>) : RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : NoteRVAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note_rview, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: NoteRVAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val txtView1: TextView = v.findViewById(R.id.noteTitle)
        private val txtView2: TextView = v.findViewById(R.id.noteContent)

        fun bind(item: Note) {
            txtView1.text = item.title
            txtView2.text = item.content
        }
    }
}