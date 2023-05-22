package com.example.weatherforecast.ui.note

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.room.Room
import com.example.weatherforecast.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private lateinit var rcView: RecyclerView
class NoteActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "notes.db"
        ).build()
    }
    private val viewModel by viewModels<NoteViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NoteViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteTheme{
                val state by viewModel.state.collectAsState()
                NoteScreen(state = state, onEvent = viewModel::onEvent)
            }
        }
//        setContentView(R.layout.activity_note)
//        findViewById<ImageView>(R.id.backBtn).setOnClickListener {
//            homeScreen()
//        }
//        rcView = findViewById(R.id.noteRV)
//        rcView.layoutManager = LinearLayoutManager(this)
//        lifecycleScope.launch {
//            viewModel.state.collectLatest {
//                rcView.swapAdapter(NoteRVAdapter(it.notes), false)
//            }
//        }
//
//        findViewById<FloatingActionButton>(R.id.floatBtn).setOnClickListener{
//            NoteEvent.ShowNote()
//        }
    }
    private fun homeScreen() {finish()}
}