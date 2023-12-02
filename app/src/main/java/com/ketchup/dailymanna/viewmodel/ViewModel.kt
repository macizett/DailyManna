package com.ketchup.dailymanna.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchup.dailymanna.MannaTextDao
import com.ketchup.dailymanna.model.MannaTextEntity
import com.ketchup.dailymanna.repositories.MannaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModel(var mannaTextDao: MannaTextDao, var application: Application, var context: Context) : ViewModel() {

    private val _allMannaTexts = MutableLiveData<List<MannaTextEntity>>()
    val allMannaTexts: LiveData<List<MannaTextEntity>> = _allMannaTexts

    private val _allFavoriteMannaTexts = MutableLiveData<List<MannaTextEntity>>()
    val allFavoriteMannaTexts: LiveData<List<MannaTextEntity>> = _allFavoriteMannaTexts

    val mannaRepository = MannaRepository(mannaTextDao)

    private val sharedPreferences = application.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

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

    fun shareText(mannaText: MannaTextEntity) {
        val sharedText = "${mannaText.title}\r\n\r\nFragment:\r\n\r\n${mannaText.bibleText}\r\n\r\n\r\nRozważanie:\r\n\r\n${mannaText.text}\r\n\r\n\r\n".trimIndent()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, sharedText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
}


