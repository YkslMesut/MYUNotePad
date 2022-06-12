package com.mesutyukselusta.myunotepad.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mesutyukselusta.myunotepad.db.NoteDatabase
import com.mesutyukselusta.myunotepad.model.Note
import com.mesutyukselusta.myunotepad.model.Reminder
import com.mesutyukselusta.myunotepad.util.AlarmReceiver
import com.mesutyukselusta.myunotepad.util.AlarmService
import kotlinx.coroutines.launch
import java.util.*

class EditNoteViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = "EditNoteViewModel"

    val noteLiveData = MutableLiveData<Note>()
    val reminderLiveData = MutableLiveData<Reminder>()
    val reminderExistControl = MutableLiveData<Boolean>()
    val updateControl = MutableLiveData<Boolean>()
    val deleteControl = MutableLiveData<Boolean>()

    fun getNote(uuid: Int) {
        launch {
            val note = NoteDatabase(getApplication()).noteDao().getNote(uuid)
            showNote(note)
        }
    }

    private fun showNote(note: Note) {
        noteLiveData.value = note
    }
    private fun showReminder(reminder: Reminder) {
        reminderLiveData.value = reminder
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

    fun deleteNoteFromRoom(uuid : Int){
        launch {
            NoteDatabase(getApplication()).noteDao().deleteNote(uuid)
            updateControl.value = true
        }
    }

    fun setAlarm(calendar: Calendar, reminderMessage: String, dialog: Dialog, context: Context,selectedNoteUuid: Int,alarmService: AlarmService){

        alarmService.setExactAlarm(calendar.timeInMillis,reminderMessage)


        //Control have note got Reminder
        getReminderAndDoAction(reminderMessage,calendar.time.toString(),selectedNoteUuid)

        Toast.makeText(context,"AlarmSetSuccesfully", Toast.LENGTH_LONG).show()
        reminderExistControl.value = true
        dialog.dismiss()

    }


    private fun insertReminder(reminderTitle: String,reminderDate: String, reminderUuid : Int){
        val reminder = Reminder(reminderTitle,reminderDate,reminderUuid)
        val dao = NoteDatabase(getApplication()).noteDao()
        launch {
            dao.insertReminder(reminder)
        }
    }

    private fun getReminderAndDoAction(reminderMessage: String, reminderDate: String, uuid: Int) {
        launch {
            val reminder = NoteDatabase(getApplication()).noteDao().getReminder(uuid)
            if (reminder != null) {
                updateReminder(reminderMessage,reminderDate,uuid)
            } else {
                insertReminder(reminderMessage,reminderDate,uuid)
            }
        }
    }

    fun getReminder(uuid: Int) {
        launch {
            val reminder = NoteDatabase(getApplication()).noteDao().getReminder(uuid)
            if (reminder != null) {
                reminderExistControl.value = true
                showReminder(reminder)
            } else {
                reminderExistControl.value = false
            }
        }
    }

    private fun updateReminder(reminderTitle: String, reminderDate: String,reminderUuid : Int) {
            val updatedReminder = Reminder(reminderTitle, reminderDate,reminderUuid)
            launch {
                NoteDatabase(getApplication()).noteDao().updateReminder(updatedReminder)
            }

    }


}
