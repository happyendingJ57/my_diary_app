package com.example.app_my_diary.diary.calendar.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_my_diary.base.BaseViewHolder
import com.example.app_my_diary.databinding.ItemDateDiaryBinding
import com.example.app_my_diary.model.DiaryModel
import com.example.app_my_diary.utils.clickWithDebounce

@SuppressLint("NotifyDataSetChanged")
class DiaryAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    val data = mutableListOf<DiaryModel>()
    lateinit var listener: OnDiaryClickListener

    fun updateData(list: MutableList<DiaryModel>) {
        if (list.isNotEmpty()) {
            data.clear()
            data.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemBinding =
            ItemDateDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class DiaryViewHolder(private val binding: ItemDateDiaryBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.apply {
                diaryModel = data[position]
                root.clickWithDebounce {
                    listener.onDiaryClickListener(data[position])
                }
            }
        }
    }

    interface OnDiaryClickListener {
        fun onDiaryClickListener(diaryModel: DiaryModel)
    }
}