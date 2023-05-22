package com.example.weatherforecast.ui.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note (
    val title: String = "",
    val content: String = "",
    val pinned: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)