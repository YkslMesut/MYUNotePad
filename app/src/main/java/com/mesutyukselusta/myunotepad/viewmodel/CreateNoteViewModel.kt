package com.mesutyukselusta.myunotepad.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mesutyukselusta.myunotepad.db.NoteDatabase
import com.mesutyukselusta.myunotepad.model.Note
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteViewModel(application: Application) : BaseViewModel(application) {

    val createNoteInputControl = MutableLiveData<Boolean>()
    val createNoteInsertControl = MutableLiveData<Boolean>()
    private val TAG = "CreateNoteViewModel"

    fun validateControl(noteTitle : String,noteDesc : String)  {
        createNoteInputControl.value = noteTitle.isNotEmpty()  && noteDesc.isNotEmpty()

        if (createNoteInputControl.value == true) {
            insertPayer(noteTitle,noteDesc)
        }

    }

    private fun insertPayer(noteTitle: String,noteDesc: String){
        Log.d(TAG, "insertPayer: InsertPayerFunc")
        val today = currentDate()
        val note = Note(noteDesc,noteTitle,today)
        val dao = NoteDatabase(getApplication()).noteDao()
        launch {
            Log.d(TAG, "insertPayerLaunch: ")
            dao.insertNote(note)
            createNoteInsertControl.value = true
        }
    }

    private fun currentDate(): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }
}