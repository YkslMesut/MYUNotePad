package com.mesutyukselusta.myunotepad.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mesutyukselusta.myunotepad.db.NoteDatabase
import com.mesutyukselusta.myunotepad.model.Note
import kotlinx.coroutines.launch

class NoteFeedViewModel(application: Application) : BaseViewModel(application) {

    val noteListLiveData = MutableLiveData<List<Note>>()
    val noteError = MutableLiveData<Boolean>()
    val noteLoading = MutableLiveData<Boolean>()

    fun getAllNotesFromRoom() {
        launch {
            val notes = NoteDatabase(getApplication()).noteDao().getAllNotes()
            showNotes(notes)
        }
    }

    private fun showNotes(noteList: List<Note>) {
        noteListLiveData.value = noteList
        noteError.value = false
        noteLoading.value = false
    }

    fun deleteNoteFromRoom(uuid : Int){
        launch {
            NoteDatabase(getApplication()).noteDao().deleteNote(uuid)
            getAllNotesFromRoom()
        }
    }
}