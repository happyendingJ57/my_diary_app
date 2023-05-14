package com.example.app_my_diary.ui.event

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.app_my_diary.R
import com.example.app_my_diary.base.AbsBaseFragment
import com.example.app_my_diary.databinding.FragmentEventBinding
import com.example.app_my_diary.ui.event.adapter.EventViewPagerAdapter
import com.example.app_my_diary.ui.event.eventdialog.EventDiaLog
import com.example.app_my_diary.utils.setSafeMenuClickListener
import com.example.app_my_diary.ui.event.myevent.MyEventFragment
import com.example.app_my_diary.utils.SystemUtils
import com.google.android.material.tabs.TabLayoutMediator

class EventFragment : AbsBaseFragment<FragmentEventBinding>(), EventDiaLog.OnDoneClick {
    private var mAdapter: EventViewPagerAdapter? = null
    private var fragments = mutableListOf<Fragment>()
    private var eventModel = EventModel(0, "", "", "", "", "", 0L, 2, 0L)

    private val eventDialog by lazy {
        EventDiaLog.create(isAdd = true,"Thêm sự kiện",eventModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPage()
    }

    override fun initView() {
        mAdapter = EventViewPagerAdapter(this, fragments)
        binding!!.apply {
            viewPager.apply {
                offscreenPageLimit = fragments.size
                adapter = mAdapter
            }
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            toolbar.setSafeMenuClickListener { menuItem ->
                if (menuItem!!.itemId == R.id.action_add_event) {
                    if (!eventDialog.isAdded) {
                        eventDialog.setOnDoneClick(this@EventFragment)
                        eventDialog.show(requireActivity().supportFragmentManager, "addEvent")
                    }
                }
            }
        }

        binding!!.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 1) {
                    (fragments[position] as MyEventFragment).fetchData()
                }
            }
        })
    }

    override fun getLayout(): Int {
        return R.layout.fragment_event
    }

    fun initPage() {
        fragments.clear()
        fragments.add(MyEventFragment())
    }

    override fun onDoneClick(eventModel: EventModel) {
        (fragments[0] as MyEventFragment).fetchData()
        findNavController().navigate(
            EventFragmentDirections.actionEventFragmentToEventDetailFragment(
                eventModel
            )
        )
    }
}