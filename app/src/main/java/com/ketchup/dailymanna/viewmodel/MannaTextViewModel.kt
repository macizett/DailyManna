package com.ketchup.dailymanna.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchup.dailymanna.AppDatabase
import com.ketchup.dailymanna.MannaTextDao
import com.ketchup.dailymanna.model.MannaTextEntity
import com.ketchup.dailymanna.repositories.MannaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MannaTextViewModel(var mannaTextDao: MannaTextDao) : ViewModel() {

    private val _allMannaTexts = MutableLiveData<List<MannaTextEntity>>()
    val allMannaTexts: LiveData<List<MannaTextEntity>> = _allMannaTexts

    // Function to load all manna texts
    fun loadAllMannaTexts() {
        viewModelScope.launch {
            val texts = MannaRepository(mannaTextDao).getAllMannaTexts()
            _allMannaTexts.postValue(texts)
        }
    }
}


