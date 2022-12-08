package com.example.app_my_diary.diary.diarydetaildialog

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app_my_diary.R
import com.example.app_my_diary.app.App
import com.example.app_my_diary.base.BaseViewModelDialogFragment
import com.example.app_my_diary.databinding.LayoutDiaryDetailDialogBinding
import com.example.app_my_diary.dialog.ImageDetailDialog
import com.example.app_my_diary.dialog.MessageAlertDialog
import com.example.app_my_diary.diary.eventdiarylovedialog.AddDiaryLoveDialog
import com.example.app_my_diary.model.DiaryModel
import com.example.app_my_diary.model.PhotoModel
import com.example.app_my_diary.utils.*

class DiaryDetailDialog : BaseViewModelDialogFragment<LayoutDiaryDetailDialogBinding>(),
    ImageAdapter.OnPickPhotoItemClickListener, AddDiaryLoveDialog.OnDoneClickListener,
    View.OnClickListener {

    var idDiary: Int? = null
    var status: Int? = null
    var titleDialog: String? = null
    var diaryModel: DiaryModel? = null

    private var checkNext = false
    private var checkPrevious = false
    private val pickPhotoAdapter = ImageAdapter()
    private lateinit var mLayoutManager: GridLayoutManager
    private var columns: Int = Constants.AT_LEAST_COLUMN
    private lateinit var viewModel: DiaryDetailViewModel
    var onDeleteDiary: OnDeleteDiary? = null
    override fun initView() {
        initLayoutManager()
        binding!!.apply {
            toolbar!!.apply {
                title = titleDialog
                setNavigationOnClickListener { dismiss() }
                setSafeMenuClickListener {
                    when (it!!.itemId) {
                        R.id.action_delete -> {
                            showMessageDialogDeleteDiary(diaryModel!!)
                        }
                        R.id.action_edit -> {
                            val dialog = AddDiaryLoveDialog.createEdit(
                                "Edit Diary Love",
                              2,
                                diaryModel!!
                            )
                            dialog.listener = this@DiaryDetailDialog
                            dialog.show(parentFragmentManager, "diaryDialog")
                        }
                    }
                }
            }
            btnPrevious.setOnClickListener(this@DiaryDetailDialog)
            btnNext.setOnClickListener(this@DiaryDetailDialog)
            rvPickPhoto.apply {
                adapter = pickPhotoAdapter
                layoutManager = mLayoutManager
            }
        }
        setUpButton()
        viewModel.fetchData(type = 2, idOld = idDiary!!)
    }

    fun setUpButton() {
        if (status == 2) {
            isPreviousEnabled()
            isNotNextEnabled()
        } else if (status == 1) {
            isNextEnabled()
            isNotPreviousEnabled()
        } else if (status == 4) {
            isNextEnabled()
            isPreviousEnabled()
        }else{
            isNotNextEnabled()
            isNotPreviousEnabled()
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnPrevious -> {
//                viewModel.checkPreviousData(idOld = idDiary!!)
                viewModel.fetchData(type = 1, idOld = idDiary!!)
//                viewModel.checkNextData(idOld = idDiary!!)
//                if (checkPrevious == true) {
//                    isNotPreviousEnabled()
//                } else {
//                    isPreviousEnabled()
//                }
            }
            R.id.btnNext -> {
//                viewModel.checkNextData(idOld = idDiary!!)
                viewModel.fetchData(type = 3, idOld = idDiary!!)
//                viewModel.checkPreviousData(idOld = idDiary!!)
//                if (checkNext == true) {
//                    isNotNextEnabled()
//                } else {
//                    isNextEnabled()
//                }
            }
        }
    }


    override fun getLayout(): Int {
        return R.layout.layout_diary_detail_dialog
    }

    override fun initViewModel() {
        idDiary = requireArguments().getInt(KEY_ID)
        status = requireArguments().getInt(KEY_STATUS)
        titleDialog = requireArguments().getString(KEY_TITLE)
        val factory = DiaryDetailViewModel.Factory(mainActivity.application)
        viewModel = ViewModelProvider(this, factory)[DiaryDetailViewModel::class.java]
        viewModel!!.uiData.observe(this) {
            it?.let {
                when (it.loadDataStatus) {
                    LoadDataStatus.SUCCESS -> {
                        val diaryList = (it as DataResponse.DataSuccessResponse).body
                        idDiary = diaryList.diaryId
                        val listImage = Utils.jsonToList(diaryList!!.images)
                        if (listImage.isEmpty()) {
                            binding!!.rvPickPhoto.visibility = View.GONE
                        } else {
                            binding!!.rvPickPhoto.visibility = View.VISIBLE
                            pickPhotoAdapter!!.updateData(listImage)
                            pickPhotoAdapter.onPickPhotoItemClickListener = this@DiaryDetailDialog
                        }
                        diaryModel = DiaryModel(
                            idDiary,
                            diaryList.title,
                            diaryList.description,
                            diaryList.images,
                            diaryList.time,
                            diaryList.isDate,
                            diaryList.date
                        )
                        viewModel.checkNextData(idOld = diaryList.diaryId!!)
                        viewModel.checkPreviousData(idOld = diaryList.diaryId!!)
                        binding!!.diaryModel = diaryList
                    }
                    LoadDataStatus.ERROR -> {
                    }
                    else -> {
                    }
                }
            }
        }
        viewModel!!.checkPreviousData.observe(this) {
            it?.let {
                when (it.loadDataStatus) {
                    LoadDataStatus.SUCCESS -> {
                        isNotPreviousEnabled()
                    }
                    LoadDataStatus.ERROR -> {
                        isPreviousEnabled()
                    }
                    else -> {
                    }
                }
            }
        }

        viewModel!!.checkNextData.observe(this) {
            it?.let {
                when (it.loadDataStatus) {
                    LoadDataStatus.SUCCESS -> {
                        isNotNextEnabled()
                    }
                    LoadDataStatus.ERROR -> {
                        isNextEnabled()
                    }
                    else -> {
                    }
                }
            }
        }
        viewModel.fetchData(type = 2, idOld = idDiary!!)
    }

    private fun initLayoutManager() {
        mLayoutManager = GridLayoutManager(mainActivity, columns)
    }

    fun isPreviousEnabled() {
        binding!!.btnPrevious.isEnabled = false
        binding!!.ispreviousEnabled = false
        checkPrevious = false
    }

    fun isNotPreviousEnabled() {
        binding!!.btnPrevious.isEnabled = true
        binding!!.ispreviousEnabled = true
        checkPrevious = true
    }

    fun isNotNextEnabled() {
        binding!!.btnNext.isEnabled = true
        binding!!.isnextEnabled = true
        checkNext = true
    }

    fun isNextEnabled() {
        binding!!.btnNext.isEnabled = false
        binding!!.isnextEnabled = false
        checkNext = false
    }

    fun showMessageDialogDeleteDiary(diaryModel: DiaryModel) {
        val message = getString(R.string.string_diary_delete)
        val title = getString(R.string.string_story_title_delete)
        val messageAlertDialog = MessageAlertDialog.create(title, message)
        messageAlertDialog.onAlertMessageDialogClickListener =
            object : MessageAlertDialog.OnAlertMessageDialogClickListener {
                override fun onPositive() {
                    viewModel.deleteDiary(diaryModel = diaryModel)
                    if (checkNext == false && checkPrevious == true) {
                        viewModel.fetchData(type = 1, idOld = idDiary!!)
//                        viewModel.checkPreviousData(idOld = idDiary!!)
//                        viewModel.checkNextData(idOld = idDiary!!)
                    } else if (checkNext == false && checkPrevious == false) {
                        dismiss()
                    } else {
                        viewModel.fetchData(type = 3, idOld = idDiary!!)
//                        viewModel.checkNextData(idOld = idDiary!!)
//                        viewModel.checkPreviousData(idOld = idDiary!!)
                    }
                    onDeleteDiary!!.onDeleteDiary()
                }

                override fun onNegative() {
                    messageAlertDialog.dismiss()
                }
            }
        if (!messageAlertDialog.isAdded) {
            messageAlertDialog.show(parentFragmentManager, "DeleteDiary")
        }
    }


    companion object {
        const val KEY_ID = "Key_Id"
        const val KEY_TITLE = "Key_Title"
        const val KEY_STATUS = "Key_Status"
        fun create(id: Int?, title: String, status: Int): DiaryDetailDialog {
            val dialog = DiaryDetailDialog()
            val bundle = Bundle()
            bundle.putInt(KEY_ID, id!!)
            bundle.putString(KEY_TITLE, title)
            bundle.putInt(KEY_STATUS, status)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun onClickItemRootClick(position: Int, imageList: MutableList<PhotoModel>) {
        val dialog = ImageDetailDialog.create(position, imageList)
//        if (!dialog.isAdded) {
        dialog.show(parentFragmentManager, "ImageDiaryDetail")
//        }
    }

    interface OnDeleteDiary {
        fun onDeleteDiary()
    }

    override fun onSavedDone() {
        viewModel.fetchData(2, idDiary!!)
        onDeleteDiary!!.onDeleteDiary()
    }
}