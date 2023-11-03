package com.ketchup.dailymanna

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ketchup.dailymanna.model.MannaTextEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MannaTextDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(mannaTexts: List<MannaTextEntity>)

    @Query("SELECT * FROM manna_texts WHERE id = :id")
    fun getMannaTextById(id: Int): MannaTextEntity

    @Query("SELECT * FROM manna_texts")
    fun getAllMannaTexts(): List<MannaTextEntity>

    @Query("SELECT * FROM manna_texts WHERE isFavorite = 1")
    fun getAllFavorites(): List<MannaTextEntity>
    @Update
    fun updateMannaText(mannaText: MannaTextEntity)
}
