package com.ketchup.dailymanna.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchup.dailymanna.VerseDao
import com.ketchup.dailymanna.model.VerseEntity
import com.ketchup.dailymanna.repositories.VerseRepository
import kotlinx.coroutines.launch

class VerseViewModel(var  verseDao: VerseDao) : ViewModel() {
    private val _currentVerses = MutableLiveData<List<VerseEntity>>()
    val currentVerses: LiveData<List<VerseEntity>> = _currentVerses

    // Function to load verses based on range
    fun getVersesInRange(bookID: Int, chapterFrom: Int, chapterTo: Int, verseFrom: Int, verseTo: Int) {
        viewModelScope.launch {
            val verses = VerseRepository(verseDao).getVersesInRange(bookID, chapterFrom, chapterTo, verseFrom, verseTo)
            _currentVerses.postValue(verses)
        }
    }

}
