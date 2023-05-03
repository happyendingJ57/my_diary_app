package com.example.app_my_diary.ui.event.base

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentListEventBinding
import com.example.app_my_diary.utils.DataResponse
import com.example.app_my_diary.utils.LoadDataStatus
import com.example.app_my_diary.ui.event.adapter.EventListAdapter


abstract class BaseEventFragment<V : BaseEventViewModel> :
    BaseViewModelFragment<FragmentListEventBinding>() {

    val mAdapter = EventListAdapter()
    private lateinit var mLayoutManager: LinearLayoutManager
    protected lateinit var viewModel: V

    override fun getLayout(): Int {
        return R.layout.fragment_list_event
    }

    override fun initView() {
        initLayoutManager()
        binding!!.apply {
            rvEvent.apply {
                adapter = mAdapter
                layoutManager = mLayoutManager
            }
            mViewModel = viewModel
        }
    }

    private fun initLayoutManager() {
        mLayoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
    }

    fun initObserveData() {
        viewModel.uiData.observe(this) {
            it.let {
                when (it.loadDataStatus) {
                    LoadDataStatus.SUCCESS -> {
                        val eventList = (it as DataResponse.DataSuccessResponse).body
                        mAdapter.updateData(eventList)
                    }

                    LoadDataStatus.ERROR -> {
                        mAdapter.clearData()
                    }

                    else -> {}
                }
            }
        }
        viewModel.fetchData()
    }
}