package com.ketchup.dailymanna.repositories

import com.ketchup.dailymanna.MannaTextDao
import com.ketchup.dailymanna.model.MannaTextEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MannaRepository(val mannaTextDao: MannaTextDao) {
    suspend fun getAllMannaTexts(): List<MannaTextEntity> {
        return withContext(Dispatchers.IO) {
            mannaTextDao.getAllMannaTexts()
        }
    }

    suspend fun getAllFavorites(): List<MannaTextEntity> {
        return withContext(Dispatchers.IO) {
            mannaTextDao.getAllFavorites()
        }
    }

    suspend fun updateMannaText(mannaTextEntity: MannaTextEntity) {
        withContext(Dispatchers.IO){
            mannaTextDao.updateMannaText(mannaTextEntity)
        }
    }
}