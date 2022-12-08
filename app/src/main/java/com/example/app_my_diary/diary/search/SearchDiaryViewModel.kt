package com.example.app_my_diary.diary.search

import android.app.Application
import androidx.lifecycle.*
import com.example.app_my_diary.R
import com.example.app_my_diary.model.DiaryModel
import com.example.app_my_diary.repository.DiaryRepository
import com.example.app_my_diary.utils.DataResponse
import com.example.app_my_diary.utils.LoadDataStatus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SearchDiaryViewModel(app: Application) : ViewModel() {

    private val repository = DiaryRepository(app)

    val mDiaryList =
        MutableLiveData<DataResponse<MutableList<DiaryModel>>>(DataResponse.DataEmptyResponse())

    val isLoading: LiveData<Boolean> = Transformations.map(mDiaryList) {
        mDiaryList.value!!.loadDataStatus == LoadDataStatus.LOADING
    }

    val isEmpty: LiveData<Boolean> = Transformations.map(mDiaryList) {
        mDiaryList.value!!.loadDataStatus == LoadDataStatus.ERROR
    }

    fun fetchDiary(keyword: String) {
        viewModelScope.launch {
            mDiaryList.value = DataResponse.DataLoadingResponse()
            try {
                val data = repository.getDiaryByKeyword(keyword)
                if (data != null && data.isNotEmpty()) {
                    val sdf = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.US)
                    val mData = mutableListOf<DiaryModel>()
                    for ((key, value) in data.groupBy { sdf.format(it.time) }) {
                        val dateModel = DiaryModel(date = key, isDate = true)
                        mData.add(dateModel)
                        mData.addAll(value)
                    }
                    mDiaryList.value = DataResponse.DataSuccessResponse(mData)
                } else {
                    mDiaryList.value = DataResponse.DataErrorResponse(null)
                }
            } catch (ex: Exception) {
                mDiaryList.value = DataResponse.DataErrorResponse(null)
            }
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchDiaryViewModel::class.java)) {
                return SearchDiaryViewModel(app) as T
            }
            throw IllegalArgumentException(app.resources.getString(R.string.unable_create_viewmodel))
        }
    }
}