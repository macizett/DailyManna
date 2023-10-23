package com.ketchup.dailymanna

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import com.ketchup.dailymanna.model.MannaTextEntity
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
            val bibleText = contentJsonObject.getString("bibleText")
            val id = contentJsonObject.getInt("id")


            val mannaText = MannaTextEntity(id = id, title = title, text = text, bibleText = bibleText)
            mannaTextEntities.add(mannaText)
        }

        coroutineScope.launch(Dispatchers.IO){
            dao.insertAll(mannaTextEntities)
        }
    }
}
