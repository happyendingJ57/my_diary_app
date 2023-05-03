package com.example.app_my_diary.repository

import android.app.Application
import com.example.app_my_diary.room.AppDatabase
import com.example.app_my_diary.ui.event.StoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoryRepository(private val app: Application) {
    private val storyDao = AppDatabase.getInstance(app).storyDao()

    suspend fun getAllStory(eventId: Int): List<StoryModel>? = withContext(Dispatchers.Default) {
        try {
            storyDao.getAllStory(eventId)
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun addNewStory(storyModel: StoryModel): Boolean = withContext(Dispatchers.Default) {
        try {
            storyDao.addStory(storyModel)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun updateStory(storyModel: StoryModel): Boolean = withContext(Dispatchers.Default) {
        try {
            storyDao.updateStory(storyModel)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun deleteStoryByEventID(eventId: Int) = withContext(Dispatchers.Default) {
        try {
            storyDao.deleteStoryByEventId(eventId)
        } catch (ex: Exception) {
        }
    }

    suspend fun deleteStory(storyModel: StoryModel): Boolean = withContext(Dispatchers.Default) {
        try {
            storyDao.deleteStory(storyModel)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun getStoryById(storyID: Int): StoryModel? = withContext(Dispatchers.Default) {
        try {
            storyDao.getStory(storyID)
        } catch (ex: Exception) {
            null
        }
    }
}