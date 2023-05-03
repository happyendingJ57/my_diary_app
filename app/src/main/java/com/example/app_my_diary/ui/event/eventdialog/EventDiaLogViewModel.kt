package com.example.app_my_diary.ui.event.eventdialog

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_my_diary.R
import com.example.app_my_diary.repository.EventRepository
import com.example.app_my_diary.ui.event.EventModel
import kotlinx.coroutines.launch

class EventDiaLogViewModel(val application: Application) : ViewModel() {
    var mEventModel: EventModel? = null
    private val repository = EventRepository(application)
    var isDone = MutableLiveData<Boolean>().apply { postValue(false) }

    fun addEvent(title: String, date: Long, typeDay: Int) {
        viewModelScope.launch {
            val createdDate = System.currentTimeMillis()
            val eventModel =
                EventModel(
                    title = title,
                    date = date,
                    calType = typeDay,
                    createdDate = createdDate
                )
            repository.addMyEvent(eventModel)
            mEventModel = repository.getMyEventByDate(createdDate)

            Log.d("TAG", "addEventtt: ${mEventModel}")
            if (mEventModel?.eventId != null) {
                Log.d("TAG", "addEventtt: 111111")
                isDone.postValue(true)
            }
        }
    }

    fun editEvent(id: Int, title: String, date: Long, typeDay: Int) {
        viewModelScope.launch {
            repository.updateEvent(id, title, date, typeDay)
            mEventModel = repository.getMyEventById(id)
            isDone.postValue(true)
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventDiaLogViewModel::class.java)) {
                return EventDiaLogViewModel(app) as T
            }
            throw IllegalArgumentException(app.resources.getString(R.string.unable_create_viewmodel))
        }
    }
}