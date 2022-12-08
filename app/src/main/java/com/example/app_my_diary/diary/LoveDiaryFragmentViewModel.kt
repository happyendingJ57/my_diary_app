package com.example.app_my_diary.diary

import android.app.Application
import androidx.lifecycle.*
import com.example.app_my_diary.R
import com.example.app_my_diary.model.DiaryModel
import com.example.app_my_diary.repository.DiaryRepository
import com.example.app_my_diary.utils.DataResponse
import com.example.app_my_diary.utils.LoadDataStatus
import com.example.app_my_diary.utils.RootPath
import kotlinx.coroutines.launch

class LoveDiaryFragmentViewModel(private val app: Application) : ViewModel() {
    private val repository = DiaryRepository(app)

    val uiData =
        MutableLiveData<DataResponse<MutableList<DiaryModel>>>(DataResponse.DataEmptyResponse())

    val isLoading: LiveData<Boolean> = Transformations.map(uiData) {
        uiData.value!!.loadDataStatus == LoadDataStatus.LOADING
    }

    val isEmpty: LiveData<Boolean> = Transformations.map(uiData) {
        uiData.value!!.loadDataStatus == LoadDataStatus.ERROR
    }

    val isUpdateDone = MutableLiveData<Boolean>().apply { postValue(false) }
    val isDeleteDone = MutableLiveData<Boolean>().apply { postValue(false) }

    fun fetchData() {
        if (uiData.value!!.loadDataStatus != LoadDataStatus.LOADING) {
            uiData.value = DataResponse.DataLoadingResponse()
            viewModelScope.launch {
                try {
                    val diaryList = repository.getAllDiary()
                    if (diaryList != null && diaryList.isNotEmpty()) {
                        uiData.value = DataResponse.DataSuccessResponse(diaryList.toMutableList())
                    } else {
                        uiData.value = DataResponse.DataErrorResponse(null)
                    }
                } catch (ex: Exception) {
                    uiData.value = DataResponse.DataErrorResponse(null)
                }
            }
        }
    }

    fun deleteDiary(diaryModel: DiaryModel) {
        viewModelScope.launch {
            isDeleteDone.postValue(repository.deleteDiary(diaryModel))
            val path = RootPath.getInstance()
                .getDiaryFolder(app.applicationContext, diaryModel.time)
            path.deleteRecursively()
        }
    }

    class Factory(private val app: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoveDiaryFragmentViewModel::class.java)) {
                return LoveDiaryFragmentViewModel(app) as T
            }

            throw IllegalArgumentException(app.resources.getString(R.string.unable_create_viewmodel))
        }
    }
}