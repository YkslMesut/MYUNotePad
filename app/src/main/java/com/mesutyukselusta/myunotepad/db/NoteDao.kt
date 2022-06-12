package com.mesutyukselusta.myunotepad.db

import androidx.room.*
import com.mesutyukselusta.myunotepad.model.Note
import com.mesutyukselusta.myunotepad.model.Reminder

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note:Note)


    @Query("SELECT * FROM note WHERE uuid = :uuid")
    suspend fun getNote(uuid : Int) : Note


    @Query("SELECT * FROM note")
    suspend fun getAllNotes() : List<Note>

    @Query("DELETE FROM note WHERE uuid = :uuid")
    suspend fun deleteNote(uuid : Int)


    @Update(entity = Note::class)
    suspend fun updateNote(note: Note)

    @Update(entity = Reminder::class)
    suspend fun updateReminder(reminder: Reminder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder)

    @Query("SELECT * FROM reminder WHERE reminder_id = :reminderId")
    suspend fun getReminder(reminderId : Int) : Reminder


}