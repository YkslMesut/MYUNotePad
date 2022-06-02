package com.mesutyukselusta.myunotepad.db

import androidx.room.*
import com.mesutyukselusta.myunotepad.model.Note

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
    suspend fun updateNote(payerInfo: Note)


}