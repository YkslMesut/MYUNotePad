package com.mesutyukselusta.myunotepad.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Note(
    @ColumnInfo(name = "note")
    @SerializedName("note")
    var note : String,
    @ColumnInfo(name = "note_title")
    @SerializedName("note_title")
    var note_title : String,
    @ColumnInfo(name = "note_creation_date")
    @SerializedName("note_creation_date")
    val note_creation_date : String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uuid")
    @SerializedName("uuid")
    var uuid: Int = 0
}