package com.ketchup.dailymanna.viewmodel

import android.app.Application
import android.content.Context
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

class ViewModel(var mannaTextDao: MannaTextDao, var application: Application) : ViewModel() {

    private val _allMannaTexts = MutableLiveData<List<MannaTextEntity>>()
    val allMannaTexts: LiveData<List<MannaTextEntity>> = _allMannaTexts

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
            _allMannaTexts.postValue(texts)
        }
    }

    fun setFavorite(mannaTextEntity: MannaTextEntity, state: Boolean) {
        viewModelScope.launch {
            mannaTextEntity.isFavorite = state
            mannaRepository.updateMannaText(mannaTextEntity)
        }
    }

    fun savePageIndex(index: Int) {
        sharedPreferences.edit().putInt("pageIndex", index).commit()
    }

    fun getSavedPageIndex(): Int {
        var index = sharedPreferences.getInt("pageIndex", 0)
        return index
    }
}


