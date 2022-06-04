package com.mesutyukselusta.myunotepad.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mesutyukselusta.myunotepad.db.NoteDatabase
import com.mesutyukselusta.myunotepad.model.Note
import kotlinx.coroutines.launch

class EditNoteViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = "EditNoteViewModel"

    val noteLiveData = MutableLiveData<Note>()
    val updateControl = MutableLiveData<Boolean>()

    fun getNote(uuid: Int) {
        launch {
            val note = NoteDatabase(getApplication()).noteDao().getNote(uuid)
            showNote(note)
        }
    }

    private fun showNote(note: Note) {
        noteLiveData.value = note
    }

    fun updateNote(note: Note, noteTitle: String, noteDesc: String) {
        if (noteTitle.isNotEmpty() && noteDesc.isNotEmpty()) {
            val updatedNote = Note(noteDesc, noteTitle, note.note_creation_date)
            updatedNote.uuid = note.uuid
            launch {
                NoteDatabase(getApplication()).noteDao().updateNote(updatedNote)
                updateControl.value = true
            }
        } else {
        updateControl.value = false
        }
    }
}
