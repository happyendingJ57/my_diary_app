package com.hola360.crushlovecalculator.ui.lovediary.diarydetaildialog

import android.app.Application
import androidx.lifecycle.*
import com.hola360.crushlovecalculator.R
import com.hola360.crushlovecalculator.data.model.diary.DiaryModel
import com.hola360.crushlovecalculator.data.repository.DiaryRepository
import com.hola360.crushlovecalculator.data.utils.DataResponse
import com.hola360.crushlovecalculator.data.utils.LoadDataStatus
import com.hola360.crushlovecalculator.ui.lovediary.LoveDiaryFragmentViewModel
import com.hola360.crushlovecalculator.utils.RootPath
import kotlinx.coroutines.launch

class DiaryDetailViewModel(private var app: Application) : ViewModel() {
    private var repository = DiaryRepository(app)

    val uiData = MutableLiveData<DataResponse<DiaryModel>>(DataResponse.DataEmptyResponse())
    val checkPreviousData = MutableLiveData<DataResponse<DiaryModel>>(DataResponse.DataEmptyResponse())
    val checkNextData = MutableLiveData<DataResponse<DiaryModel>>(DataResponse.DataEmptyResponse())

    val isLoading: LiveData<Boolean> = Transformations.map(uiData) {
        uiData.value!!.loadDataStatus == LoadDataStatus.LOADING
    }

    val isEmpty: LiveData<Boolean> = Transformations.map(uiData) {
        uiData.value!!.loadDataStatus == LoadDataStatus.ERROR
    }

    fun fetchData(type: Int, idOld: Int) {
        if (uiData.value!!.loadDataStatus != LoadDataStatus.LOADING) {
            uiData.value = DataResponse.DataLoadingResponse()
            viewModelScope.launch {
                try {
                    var diaryList: DiaryModel? = null
                    when (type) {
                        1 -> {
                            diaryList = repository.getDiaryPrevious(idOld)
                        }
                        2 -> {
                            diaryList = repository.getDiaryCurrent(idOld)
                        }
                        3 -> {
                            diaryList = repository.getDiaryNext(idOld)
                        }
                    }

                    if (diaryList != null) {
                        uiData.value = DataResponse.DataSuccessResponse(diaryList)
                    } else {
                        uiData.value = DataResponse.DataErrorResponse(null)
                    }
                } catch (ex: Exception) {
                    uiData.value = DataResponse.DataErrorResponse(null)
                    checkPreviousData.value = DataResponse.DataErrorResponse(null)
                    checkNextData.value = DataResponse.DataErrorResponse(null)
                }
            }
        }
    }

    fun checkPreviousData(idOld: Int) {
        if (checkPreviousData.value!!.loadDataStatus != LoadDataStatus.LOADING) {
            checkPreviousData.value = DataResponse.DataLoadingResponse()
            viewModelScope.launch {
                try {
                    var diaryList: DiaryModel? = null
                    diaryList = repository.checkPrevious(idOld)
                    if (diaryList != null) {
                        checkPreviousData.value = DataResponse.DataSuccessResponse(diaryList)
                    } else {
                        checkPreviousData.value = DataResponse.DataErrorResponse(null)
                    }
                } catch (ex: Exception) {
                    checkPreviousData.value = DataResponse.DataErrorResponse(null)
                }
            }
        }
    }

    fun checkNextData(idOld: Int) {
        if (checkNextData.value!!.loadDataStatus != LoadDataStatus.LOADING) {
            checkNextData.value = DataResponse.DataLoadingResponse()
            viewModelScope.launch {
                try {
                    var diaryList: DiaryModel? = null
                    diaryList = repository.checkNext(idOld)
                    if (diaryList != null) {
                        checkNextData.value = DataResponse.DataSuccessResponse(diaryList)
                    } else {
                        checkNextData.value = DataResponse.DataErrorResponse(null)
                    }
                } catch (ex: Exception) {
                    checkNextData.value = DataResponse.DataErrorResponse(null)
                }
            }
        }
    }

    fun deleteDiary(diaryModel: DiaryModel) {
        viewModelScope.launch {
            repository.deleteDiary(diaryModel)
            val path = RootPath.getInstance().getDiaryFolder(app.applicationContext, diaryModel.time)
            path.deleteRecursively()
        }
    }


    class Factory(private val app: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiaryDetailViewModel::class.java)) {
                return DiaryDetailViewModel(app) as T
            }
            throw IllegalArgumentException(app.resources.getString(R.string.unable_create_viewmodel))
        }
    }
}