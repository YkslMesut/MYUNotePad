package com.mesutyukselusta.myunotepad.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
    data class Reminder(
        @ColumnInfo(name = "reminder_title")
        @SerializedName("reminder_title")
        var reminder_title: String,
        @ColumnInfo(name = "reminder_date")
        @SerializedName("reminder_date")
        var reminder_date : String,
        @PrimaryKey
        @ColumnInfo(name = "reminder_id")
        @SerializedName("reminder_id")
        val reminder_id : Int
    )