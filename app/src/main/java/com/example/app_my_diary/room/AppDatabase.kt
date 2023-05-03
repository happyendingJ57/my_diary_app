package com.example.app_my_diary.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app_my_diary.model.DiaryModel
import com.example.app_my_diary.ui.event.EventModel
import com.example.app_my_diary.ui.event.StoryModel

@Database(entities = [DiaryModel::class,EventModel::class,StoryModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    abstract fun eventDao(): MyEventDao

    abstract fun storyDao(): StoryDao

    companion object {
        private const val DATABASE_NAME = "APP_DB"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}