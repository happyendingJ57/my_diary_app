package com.example.app_my_diary.ui.event.myevent

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.ActionAdapter
import com.example.app_my_diary.ListActionPopup
import com.example.app_my_diary.R
import com.example.app_my_diary.dialog.MessageAlertDialog
import com.example.app_my_diary.model.EventActionModel
import com.example.app_my_diary.ui.event.EventFragmentDirections
import com.example.app_my_diary.ui.event.EventModel
import com.example.app_my_diary.ui.event.base.BaseEventFragment
import com.example.app_my_diary.ui.event.eventdialog.EventDiaLog
import com.example.app_my_diary.utils.ToastUtils
import com.example.app_my_diary.utils.clickWithDebounce
import com.example.app_my_diary.ui.event.adapter.EventListAdapter


class MyEventFragment : BaseEventFragment<MyEventViewModel>(),
    EventListAdapter.OnMyEventItemClickListener, EventDiaLog.OnDoneClick {

    private var eventModel = EventModel(0, "", "", "", "", "", 0L, 2, 0L)
    private var isActionClicked = false
    private val actionPopup by lazy {
        ListActionPopup(mainActivity)
    }

    private val eventDialog by lazy {
        EventDiaLog.create(isAdd = true, "Thêm sự kiện", eventModel)
    }

    val actions = mutableListOf(
        EventActionModel(R.drawable.ic_action_edit, "Chỉnh sửa sự kiện"),
        EventActionModel(R.drawable.ic_action_delete, "Xóa sự kiện")
    )

    override fun initView() {
        super.initView()
        mAdapter.onMyEventClickListener = this
        binding!!.tvRetryOrAdd.clickWithDebounce {
            if (!eventDialog.isAdded) {
                eventDialog.setOnDoneClick(this@MyEventFragment)
                eventDialog.show(requireActivity().supportFragmentManager, "addEvent")
            }
        }
    }

    override fun initViewModel() {
        val factory = MyEventViewModel.Factory(mainActivity.application)
        viewModel = ViewModelProvider(this, factory)[MyEventViewModel::class.java]
        initObserveData()

        viewModel.isDeleted.observe(this) {
            it?.let {
                if (it) {
                    fetchData()
                    ToastUtils.getInstance(mainActivity).showToast("Xóa sự kiện thành công")
                }
            }
        }
    }

    override fun onItemClick(eventModel: EventModel) {
        if (!isActionClicked) {
            findNavController().navigate(
                EventFragmentDirections.actionEventFragmentToEventDetailFragment(
                    eventModel
                )
            )
        }
        isActionClicked = false
    }

    fun fetchData() {
        viewModel.fetchData()
    }

    override fun onMoreIconClick(view: View, eventModel: EventModel) {
        actionPopup.setup(view, actions, object : ActionAdapter.OnActionListener {
            override fun onItemClickListener(position: Int) {
                isActionClicked = true
                when (position) {
                    0 -> {
                        val eventDialog = EventDiaLog.create(
                            isAdd = false,
                            title = "Chỉnh sửa sự kiện",
                            eventModel = eventModel
                        )
                        if (!eventDialog.isAdded) {
                            eventDialog.setOnDoneClick(this@MyEventFragment)
                            if (isActionClicked) {
                                eventDialog.show(mainActivity.supportFragmentManager, "editDialog")
                            }
                        }
                    }
                    1 -> {
                        //show dialog confirm delete
                        showMessageDialogDeleteEvent(eventModel.eventId!!)
                    }
                }
            }
        })
    }

    override fun onDoneClick(eventModel: EventModel) {
        findNavController().navigate(
            EventFragmentDirections.actionEventFragmentToEventDetailFragment(
                eventModel
            )
        )
        fetchData()
    }


    fun showMessageDialogDeleteEvent(id: Int?) {
        val message = "Bạn có muốn xóa sự kiện này ?"
        val title = "Xóa sự kiện"
        val messageAlertDialog = MessageAlertDialog.create(title, message)
        messageAlertDialog.onAlertMessageDialogClickListener =
            object : MessageAlertDialog.OnAlertMessageDialogClickListener {
                override fun onPositive() {
                    viewModel.deleteEventById(id!!)
                }

                override fun onNegative() {
                    messageAlertDialog.dismiss()
                }
            }
        messageAlertDialog.show(parentFragmentManager, "DeletePhotos")
    }

}