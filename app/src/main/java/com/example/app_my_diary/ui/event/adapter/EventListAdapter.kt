package com.example.app_my_diary.ui.event.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_my_diary.base.BaseViewHolder
import com.example.app_my_diary.databinding.ItemMyEventBinding
import com.example.app_my_diary.ui.event.EventModel
import com.example.app_my_diary.utils.clickWithDebounce

@SuppressLint("NotifyDataSetChanged")
class EventListAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    val data = mutableListOf<EventModel>()
    var onMyEventClickListener: OnMyEventItemClickListener? = null

    @JvmName("setOnMyEventClickListener1")
    fun setOnMyEventClickListener(listener: OnMyEventItemClickListener) {
        onMyEventClickListener = listener
    }

    fun updateData(eventList: List<EventModel>?) {
        if (!eventList.isNullOrEmpty()) {
            data.clear()
            data.addAll(eventList)
            notifyDataSetChanged()
        }
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class MyEventViewHolder(val binding: ItemMyEventBinding) : BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.apply {
                myEventModel = data[position]
                root.clickWithDebounce { onMyEventClickListener?.onItemClick(data[position]) }
                ivMore.clickWithDebounce(1000) {
                    onMyEventClickListener?.onMoreIconClick(ivMore, data[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemBinding =
            ItemMyEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyEventViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnMyEventItemClickListener {
        fun onItemClick(eventModel: EventModel)
        fun onMoreIconClick(view: View, eventModel: EventModel)
    }
}