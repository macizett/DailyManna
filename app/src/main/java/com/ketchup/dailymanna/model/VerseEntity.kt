package com.ketchup.dailymanna.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verses")
data class VerseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "chapterID") val chapterID: Int,
    @ColumnInfo(name = "verseID") val verseID: Int,
    @ColumnInfo(name = "verseText") val verseText: String,
    @ColumnInfo(name = "bookID") val bookID: Int
)
