package com.ketchup.dailymanna

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import com.ketchup.dailymanna.model.MannaTextEntity
import com.ketchup.dailymanna.model.VerseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

object contentParser {

    fun parseAndInsertMannaPages(context: Context, coroutineScope: LifecycleCoroutineScope, jsonFileName: String, dao: MannaTextDao) {
        val jsonString = context.assets.open(jsonFileName).bufferedReader().use {
            it.readText()
        }

        val contentJsonArray = JSONArray(jsonString)

        val mannaTextEntities = mutableListOf<MannaTextEntity>()
        for (i in 0 until contentJsonArray.length()) {
            val contentJsonObject: JSONObject = contentJsonArray.getJSONObject(i)
            val title = contentJsonObject.getString("title")
            val text = contentJsonObject.getString("text")
            val id = contentJsonObject.getInt("id")
            val bookID = contentJsonObject.getInt("bookID")
            val chapterStart = contentJsonObject.getInt("chapterStart")
            val verseStart = contentJsonObject.getInt("verseStart")
            val chapterEnd = contentJsonObject.getInt("chapterEnd")
            val verseEnd = contentJsonObject.getInt("verseEnd")

            val mannaText = MannaTextEntity(id = id, title = title, text = text, bookID = bookID, chapterStart, verseStart, chapterEnd, verseEnd)
            mannaTextEntities.add(mannaText)
        }

        coroutineScope.launch(Dispatchers.IO){
            dao.insertAll(mannaTextEntities)
        }
    }

    fun parseAndInsertBible(context: Context, coroutineScope: LifecycleCoroutineScope, jsonFileName: String, dao: VerseDao) {
        val jsonString = context.assets.open(jsonFileName).bufferedReader().use {
            it.readText()
        }

        val bibleJsonArray = JSONArray(jsonString)

        val verseEntities = mutableListOf<VerseEntity>()
        for (i in 0 until bibleJsonArray.length()) {
            val bibleJsonObject: JSONObject = bibleJsonArray.getJSONObject(i)
            val bookID = bibleJsonObject.getInt("book_ID")
            val chapterID = bibleJsonObject.getInt("chapter_ID")
            val verseID = bibleJsonObject.getInt("verse_ID")
            val verseText = bibleJsonObject.getString("text")
            val verseEntity = VerseEntity(bookID = bookID, chapterID = chapterID, verseID = verseID, verseText = verseText)
            verseEntities.add(verseEntity)
        }
        coroutineScope.launch(Dispatchers.IO) {
            dao.insertAll(verseEntities)
        }

    }
}
