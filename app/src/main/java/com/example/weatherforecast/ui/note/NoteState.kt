package com.example.weatherforecast.ui.note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val content: String = "",
    val isAddingNote: Boolean = false,

)
