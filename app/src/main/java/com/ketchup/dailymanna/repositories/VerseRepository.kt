package com.ketchup.dailymanna.repositories

import com.ketchup.dailymanna.VerseDao
import com.ketchup.dailymanna.model.VerseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VerseRepository(val verseDao: VerseDao) {
    suspend fun getVersesInRange(bookID: Int, chapterFrom: Int, chapterTo: Int, verseFrom: Int, verseTo: Int): List<VerseEntity> {
        return withContext(Dispatchers.IO) {
            verseDao.getVersesInRange(bookID, chapterFrom, chapterTo, verseFrom, verseTo)
        }
    }
}