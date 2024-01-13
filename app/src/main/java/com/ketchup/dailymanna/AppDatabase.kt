package com.ketchup.dailymanna

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ketchup.dailymanna.model.MannaTextEntity


@Database(entities = [MannaTextEntity::class], version = 25, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mannaTextDao(): MannaTextDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}