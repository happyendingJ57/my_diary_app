package com.example.app_my_diary.diary

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_my_diary.ActionAdapter
import com.example.app_my_diary.EventActionModel
import com.example.app_my_diary.ListActionPopup
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentLoveDiaryBinding
import com.example.app_my_diary.dialog.MessageAlertDialog
import com.example.app_my_diary.diary.diarydetaildialog.DiaryDetailDialog
import com.example.app_my_diary.diary.eventdiarylovedialog.AddDiaryLoveDialog
import com.example.app_my_diary.model.DiaryModel
import com.example.app_my_diary.utils.DataResponse
import com.example.app_my_diary.utils.LoadDataStatus
import com.example.app_my_diary.utils.setSafeMenuClickListener
import com.example.app_my_diary.utils.snackbar.SnackBarType

class LoveDiaryFragment : BaseViewModelFragment<FragmentLoveDiaryBinding>(), View.OnClickListener,
    DiaryDetailDialog.OnDeleteDiary,
    AddDiaryLoveDialog.OnDoneClickListener,
    LoveDiaryAdapter.OnItemClickListener {
    private lateinit var viewModel: LoveDiaryFragmentViewModel
    private lateinit var mLayoutManager: GridLayoutManager
    private var mAdapter: LoveDiaryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = LoveDiaryAdapter(mainActivity.application)
        mAdapter!!.apply {
            setMaxColumns(2)
            setListener(this@LoveDiaryFragment)
//            imageStoryListener = this@LoveDiaryFragment
        }
    }

    private val actionPopup by lazy {
        ListActionPopup(mainActivity)
    }

    val actions = mutableListOf(
        EventActionModel(R.drawable.ic_action_edit, "Edit Diary"),
        EventActionModel(R.drawable.ic_action_delete, "Delete Diary")
    )

    override fun initView() {
        initLayoutManager()
        binding!!.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            addDiaryLove.setOnClickListener(this@LoveDiaryFragment)
            rcvDiary.apply {
                adapter = mAdapter
                layoutManager =
                    LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
                mAdapter!!.setLayoutManager(mLayoutManager)
            }
            toolbar.setSafeMenuClickListener {
                when (it!!.itemId) {
                    R.id.action_calendar -> {
                        findNavController().navigate(R.id.action_global_calendarFragment)
                    }
                    R.id.action_search -> {
                        findNavController().navigate(R.id.action_global_searchDiaryFragment)
                    }
                }
            }
        }
        viewModel.fetchData()
    }

    private fun initLayoutManager() {
        mLayoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun getLayout(): Int {
        return R.layout.fragment_love_diary
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.add_diary_love -> {
                val dialog = AddDiaryLoveDialog.create("Add Diary love", 2)
                dialog.listener = this@LoveDiaryFragment
                dialog.show(parentFragmentManager, "diaryDialog")
            }
        }
    }

    override fun initViewModel() {
        val factory = LoveDiaryFragmentViewModel.Factory(mainActivity.application)
        viewModel = ViewModelProvider(this, factory)[LoveDiaryFragmentViewModel::class.java]
        initObserveData()
        viewModel.fetchData()
    }

    fun initObserveData() {
        viewModel.uiData.observe(this) {
            it?.let {
                when (it.loadDataStatus) {
                    LoadDataStatus.SUCCESS -> {
                        val diaryList = (it as DataResponse.DataSuccessResponse).body
                        mAdapter!!.updateData(diaryList)
                        binding!!.progessbar.visibility = View.GONE
                        binding!!.layoutEmpty.visibility = View.GONE
                    }
                    LoadDataStatus.ERROR -> {
                        mAdapter!!.clearData()
                        binding!!.layoutEmpty.visibility = View.VISIBLE
                        binding!!.progessbar.visibility = View.GONE
                    }
                    else -> {
                        binding!!.progessbar.visibility = View.VISIBLE
                        binding!!.layoutEmpty.visibility = View.GONE
                    }
                }
            }
        }
        viewModel.isUpdateDone.observe(this) {
            it?.let {
                if (it) {
                    viewModel.fetchData()
                }
            }
        }

        viewModel.isDeleteDone.observe(this) {
            it?.let {
                if (it) {
                    viewModel.fetchData()
                }
            }
        }
        viewModel.fetchData()
    }

    override fun onMoreIconClick(view: View, diaryModel: DiaryModel) {
        actionPopup.setup(view, actions, object : ActionAdapter.OnActionListener {
            override fun onItemClickListener(position: Int) {
                when (position) {
                    0 -> {
                        val dialog = AddDiaryLoveDialog.createEdit(
                            "Edit Diary love",
                            2,
                            diaryModel
                        )
                        dialog.listener = this@LoveDiaryFragment
                        dialog.show(parentFragmentManager, "diaryDialog")
                    }
                    1 -> {
                        showMessageDialogDeleteDiary(diaryModel)
                    }
                }
            }

        })
    }

    fun showMessageDialogDeleteDiary(diaryModel: DiaryModel) {
        val message = getString(R.string.string_diary_delete)
        val title = getString(R.string.string_story_title_delete)
        val messageAlertDialog = MessageAlertDialog.create(title, message)
        messageAlertDialog.onAlertMessageDialogClickListener =
            object : MessageAlertDialog.OnAlertMessageDialogClickListener {
                override fun onPositive() {
                    viewModel.deleteDiary(diaryModel = diaryModel)
                }

                override fun onNegative() {
                    messageAlertDialog.dismiss()
                }
            }
        if (!messageAlertDialog.isAdded) {
            messageAlertDialog.show(parentFragmentManager, "DeleteDiary")
        }
    }

    override fun onRootItemClick(diaryModel: DiaryModel, status: Int) {
        val dialog = DiaryDetailDialog.create(diaryModel.diaryId,getString(R.string.string_read_diary),status)
        dialog.onDeleteDiary = this
        if(!dialog.isAdded){
            dialog.show(parentFragmentManager,"detailDiary")
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }

    override fun onSavedDone() {
        mainActivity.showSnackBar(
            SnackBarType.Success,
            resources.getString(R.string.title_success),
            "Post Successfully"
        )
        viewModel.fetchData()
    }

    override fun onDeleteDiary() {
       viewModel.fetchData()
    }

}