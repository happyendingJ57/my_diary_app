package com.example.app_my_diary.repository

import android.app.Application
import android.util.Log
import com.example.app_my_diary.room.AppDatabase
import com.example.app_my_diary.room.MyEventDao
import com.example.app_my_diary.ui.event.EventModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(app: Application) {
    private val eventDao: MyEventDao = AppDatabase.getInstance(app).eventDao()

    suspend fun getAllMyEvent(): List<EventModel>? = withContext(Dispatchers.Default) {
        try {
            eventDao.getAll()
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun addMyEvent(eventModel: EventModel): Boolean = withContext(Dispatchers.Default) {
        Log.d("TAG", "lkjsaljdlasdsj: 111111")
        try {
            eventDao.insert(eventModel)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun updateBgEvent(id: Int, thumb: String): Boolean = withContext(Dispatchers.Default) {
        try {
            eventDao.updateBg(id, thumb)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun updateEvent(id: Int, title: String, date: Long, type: Int) =
        withContext(Dispatchers.Default) {
            try {
                eventDao.update(id, title, date, type)
            } catch (ex: Exception) {
            }
        }

    suspend fun deleteEvent(eventID: Int): Boolean = withContext(Dispatchers.Default) {
        try {
            eventDao.delete(eventID)
            true
        } catch (ex: Exception) {
            false
        }
    }

    suspend fun getMyEventByDate(createdDate: Long): EventModel? =
        withContext(Dispatchers.Default) {
            try {
                eventDao.getMyEventByDate(createdDate)
            } catch (ex: Exception) {
                null
            }
        }

    suspend fun getMyEventById(id: Int): EventModel? = withContext(Dispatchers.Default) {
        try {
            eventDao.getMyEventById(id)
        } catch (ex: Exception) {
            null
        }
    }
}