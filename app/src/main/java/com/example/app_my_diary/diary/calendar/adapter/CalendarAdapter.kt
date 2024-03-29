package com.example.app_my_diary.diary.calendar.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_my_diary.base.BaseViewHolder
import com.example.app_my_diary.databinding.ItemDateBinding
import com.example.app_my_diary.utils.clickWithDebounce
import com.example.app_my_diary.diary.calendar.model.Date

class CalendarAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val data = mutableListOf<Date>()

    lateinit var listener: OnItemClickListener
    lateinit var listener2: OnItemClickListener2

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: MutableList<Date>) {
        if (newData.isNotEmpty()) {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemBinding =
            ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class DateViewHolder(val binding: ItemDateBinding) : BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.date = data[position]
            binding.rootLayout.clickWithDebounce {
                data[position].isSelected = !data[position].isSelected
                notifyItemChanged(position)

                data.forEachIndexed { index, date ->
                    if (date.isSelected && index != position) {
                        date.isSelected = !date.isSelected
                        notifyItemChanged(index)
                    }
                }

                if (data[position].isSelected) {
                    listener.onItemClickListener(data[position])
                    listener2.onItemClickListener(data[position])
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClickListener(date: Date)
    }

    interface OnItemClickListener2 {
        fun onItemClickListener(date: Date)
    }
}