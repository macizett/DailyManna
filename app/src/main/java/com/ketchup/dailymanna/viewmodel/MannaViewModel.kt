package com.ketchup.dailymanna.viewmodel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.speech.tts.TextToSpeech
import androidx.core.view.ContentInfoCompat.Flags
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchup.dailymanna.MannaTextDao
import com.ketchup.dailymanna.model.MannaTextEntity
import com.ketchup.dailymanna.repositories.MannaRepository
import kotlinx.coroutines.launch



class MannaViewModel(var mannaTextDao: MannaTextDao, var sharedPreferences: SharedPreferences, var textToSpeech: TextToSpeech) : ViewModel() {

    private val _allMannaTexts = MutableLiveData<List<MannaTextEntity>>()
    val allMannaTexts: LiveData<List<MannaTextEntity>> = _allMannaTexts

    private val _allFavoriteMannaTexts = MutableLiveData<List<MannaTextEntity>>()
    val allFavoriteMannaTexts: LiveData<List<MannaTextEntity>> = _allFavoriteMannaTexts

    val mannaRepository = MannaRepository(mannaTextDao)

    fun loadAllMannaTexts() {
        viewModelScope.launch {
            val texts = mannaRepository.getAllMannaTexts()
            _allMannaTexts.postValue(texts)
        }
    }

    suspend fun getAllFavorites() {
        viewModelScope.launch {
            val texts = mannaRepository.getAllFavorites()
            _allFavoriteMannaTexts.postValue(texts)
        }
    }

    fun setFavorite(mannaTextEntity: MannaTextEntity, state: Boolean) {
        viewModelScope.launch {
            mannaTextEntity.isFavorite = state
            mannaRepository.updateMannaText(mannaTextEntity)
        }
    }

    fun savePageIndex(index: Int) {
        sharedPreferences.edit().putInt("pageIndex", index).apply()
    }

    fun getSavedPageIndex(): Int {
        var index = sharedPreferences.getInt("pageIndex", 0)
        return index
    }

    fun savePageBookID(index: Int) {
        sharedPreferences.edit().putInt("pageID", index).apply()
    }

    fun getSavedPageBookID(): Int {
        var index = sharedPreferences.getInt("pageID", 0)
        return index
    }

    fun shareText(mannaText: MannaTextEntity, context: Context) {
        val sharedText = "${mannaText.title}\r\n\r\nFragment:\r\n\r\n${mannaText.bibleText}\r\n\r\n\r\nRozważanie:\r\n\r\n${mannaText.text}\r\n\r\n\r\n".trimIndent()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.EXTRA_TEXT, sharedText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun readText(text: String){
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun stopReading(){
        textToSpeech.stop()
    }
}


