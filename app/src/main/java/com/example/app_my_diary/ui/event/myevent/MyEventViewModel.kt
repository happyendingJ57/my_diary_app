package com.example.app_my_diary.ui.event.myevent

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_my_diary.R
import com.example.app_my_diary.repository.StoryRepository
import com.example.app_my_diary.ui.event.base.BaseEventViewModel
import com.example.app_my_diary.utils.DataResponse
import com.example.app_my_diary.utils.LoadDataStatus
import com.example.app_my_diary.utils.RootPath
import kotlinx.coroutines.launch

class MyEventViewModel(private val app: Application) : BaseEventViewModel(app) {

    val isDeleted = MutableLiveData<Boolean>().apply { postValue(false) }
    private val storyRepository = StoryRepository(app)

    override fun fetchData() {
        if (uiData.value!!.loadDataStatus != LoadDataStatus.LOADING) {
            uiData.value = DataResponse.DataLoadingResponse()
            viewModelScope.launch {
                try {
                    val eventList = repository.getAllMyEvent()
                    if (eventList != null && eventList.isNotEmpty()) {
                        uiData.value = DataResponse.DataSuccessResponse(eventList.toMutableList())
                    } else {
                        uiData.value = DataResponse.DataErrorResponse(null)
                    }
                } catch (ex: Exception) {
                    uiData.value = DataResponse.DataErrorResponse(null)
                }
            }
        }
    }

    override fun checkEmptyData(): Boolean {
        return (uiData.value!!.loadDataStatus == LoadDataStatus.ERROR)
    }

    fun deleteEventById(eventId: Int) {
        viewModelScope.launch {
            storyRepository.deleteStoryByEventID(eventId)
            isDeleted.postValue(repository.deleteEvent(eventId))
            val path = RootPath.getInstance().getEventIdFolder(app.applicationContext, eventId)
            path.deleteRecursively()
        }
    }

    override fun emptyMessage(): String {
        return ""
    }

    override fun buttonText(): String {
        return "Thêm sự kiện của bạn"
    }

    override fun emptyTitle(): String {
        return "Không có sự kiện nào"
    }

    override fun isCommonEvent(): Boolean {
        return false
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyEventViewModel::class.java)) {
                return MyEventViewModel(app) as T
            }
            throw IllegalArgumentException(app.resources.getString(R.string.unable_create_viewmodel))
        }
    }
}