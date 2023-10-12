package com.ketchup.dailymanna

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ketchup.dailymanna.model.VerseEntity

@Dao
interface VerseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(verses: List<VerseEntity>)

    @Query("SELECT * FROM verses WHERE bookID = :book AND chapterID = :chapter")
    fun getChapter(book: String, chapter: Int): List<VerseEntity>

    @Query("""
    SELECT * FROM verses 
    WHERE bookID = :bookID 
    AND (
        (chapterID > :startChapter AND chapterID < :endChapter) OR 
        (chapterID = :startChapter AND verseID >= :startVerse AND (chapterID < :endChapter OR (chapterID = :endChapter AND verseID <= :endVerse))) OR 
        (chapterID = :endChapter AND verseID <= :endVerse AND chapterID > :startChapter)
    )
""")
    fun getVersesInRange(bookID: Int, startChapter: Int, endChapter: Int, startVerse: Int, endVerse: Int): List<VerseEntity>


    @Query("SELECT * FROM verses")
    fun getAllVerses(): List<VerseEntity>
}
