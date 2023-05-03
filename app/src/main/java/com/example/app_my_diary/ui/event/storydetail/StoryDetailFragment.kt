package com.example.app_my_diary.ui.event.storydetail

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app_my_diary.ActionAdapter
import com.example.app_my_diary.ListActionPopup
import com.example.app_my_diary.R
import com.example.app_my_diary.app.App
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.base.StoryImageAdapter
import com.example.app_my_diary.databinding.FragmentStoryDetailBinding
import com.example.app_my_diary.dialog.MessageAlertDialog
import com.example.app_my_diary.model.PhotoModel
import com.example.app_my_diary.ui.event.EventModel
import com.example.app_my_diary.ui.event.StoryModel
import com.example.app_my_diary.ui.event.dialog.StoryDialog
import com.example.app_my_diary.utils.Constants
import com.example.app_my_diary.utils.Utils
import com.example.app_my_diary.utils.clickWithDebounce

class StoryDetailFragment : BaseViewModelFragment<FragmentStoryDetailBinding>(),
    StoryImageAdapter.OnPhotoStoryClickListener, StoryDialog.OnDoneClickListener {

    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var app: App
    private lateinit var mEventModel: EventModel
    private lateinit var mStoryModel: StoryModel
    private lateinit var mAdapter: StoryImageAdapter
    private lateinit var mViewModel: StoryDetailViewModel
    private val actionPopup by lazy {
        ListActionPopup(mainActivity)
    }

    private val args: StoryDetailFragmentArgs by lazy {
        StoryDetailFragmentArgs.fromBundle(requireArguments())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as App
        mAdapter = StoryImageAdapter(app.storyImageColumns, true)
        mEventModel = args.eventModel!!
        mStoryModel = args.storyModel!!
        initLayoutManager()
    }

    override fun initView() {
        binding!!.apply {
            eventModel = mEventModel
            storyModel = mStoryModel
            rvImages.apply {
                layoutManager = mLayoutManager
                mAdapter.apply {
                    updateData(Utils.jsonToList(mStoryModel.images))
                    mAdapter.listener = this@StoryDetailFragment
                }
                adapter = mAdapter
            }
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            ivMore.clickWithDebounce { 
                actionPopup.setup(
                    ivMore,
                    Constants.ACTION_STORY,
                    object : ActionAdapter.OnActionListener {
                        override fun onItemClickListener(position: Int) {
                            when (position) {
                                0 -> {
                                    val editStoryDialog = StoryDialog.create(
                                        "Edit Story",
                                        args.eventModel?.eventId!!,
                                        app.storyImageColumns,
                                        mStoryModel
                                    )
                                    editStoryDialog.listener = this@StoryDetailFragment
                                    if (!editStoryDialog.isAdded) {
                                        editStoryDialog.show(
                                            mainActivity.supportFragmentManager,
                                            "editStory"
                                        )
                                    }
                                }
                                1 -> {
                                    showMessageDialogDeleteStory(mStoryModel)
                                }
                            }
                        }
                    })
            }
        }
    }

    fun showMessageDialogDeleteStory(storyModel: StoryModel) {
        val message = "Bạn có muốn xóa câu chuyện này?. Nó không thể được hoàn tác"
        val title = getString(R.string.string_story_title_delete)
        val messageAlertDialog = MessageAlertDialog.create(title, message)
        messageAlertDialog.onAlertMessageDialogClickListener =
            object : MessageAlertDialog.OnAlertMessageDialogClickListener {
                override fun onPositive() {
                    mViewModel.deleteStory(storyModel = storyModel)
                }

                override fun onNegative() {
                    messageAlertDialog.dismiss()
                }
            }
        if (!messageAlertDialog.isAdded) {
            messageAlertDialog.show(parentFragmentManager, "DeleteStory")
        }
    }

    private fun initLayoutManager() {
        mLayoutManager = GridLayoutManager(requireContext(), app.storyImageColumns - 1)
    }

    override fun getLayout(): Int {
        return R.layout.fragment_story_detail
    }

    override fun initViewModel() {
        val factory = StoryDetailViewModel.Factory(mainActivity.application)
        mViewModel = ViewModelProvider(this, factory)[StoryDetailViewModel::class.java]

        mViewModel.isDeleteDone.observe(this) {
            it?.let {
                if (it) {
                    findNavController().navigateUp()
                }
            }
        }

        mViewModel.storyModel.observe(this){
            it?.let {
                binding!!.storyModel = it
                mAdapter.updateData(Utils.jsonToList(it.images))
            }
        }
    }

    override fun onPhotoStoryClickListener(position: Int, imageList: MutableList<PhotoModel>) {
        val dialog = ImageDetailDialog.create(position, imageList)
        if (!dialog.isAdded) {
            dialog.show(parentFragmentManager, "StoryDetail")
        }
    }

    override fun onSavedDone() {
        mViewModel.getStory(mStoryModel.storyId!!)
    }
}