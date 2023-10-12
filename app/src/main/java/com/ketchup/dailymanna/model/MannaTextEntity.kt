package com.ketchup.dailymanna.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manna_texts")
data class MannaTextEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "bookID") val bookID: Int,
    @ColumnInfo(name = "chapterStart") val chapterStart: Int,
    @ColumnInfo(name = "verseStart") val verseStart: Int,
    @ColumnInfo(name = "chapterEnd") val chapterEnd: Int,
    @ColumnInfo(name = "verseEnd") val verseEnd: Int,
)