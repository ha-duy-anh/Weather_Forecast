package com.example.weatherforecast.ui.note

sealed interface NoteEvent{
    object SaveNote: NoteEvent
    data class SetTitle(val title: String): NoteEvent
    data class SetContent(val content: String): NoteEvent
    object ShowNote: NoteEvent {
        operator fun invoke() {
            ShowNote
        }
    }

    object HideNote: NoteEvent
    data class DeleteNote(val note: Note): NoteEvent
}