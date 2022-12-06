package com.example.app_my_diary.diary.diarydetaildialog

import android.annotation.SuppressLint
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_my_diary.base.BaseViewHolder
import com.example.app_my_diary.model.PhotoModel


class ImageAdapter() : RecyclerView.Adapter<BaseViewHolder>(){

    var onPickPhotoItemClickListener: OnPickPhotoItemClickListener? = null

    val data = mutableListOf(
        PhotoModel(isNormalItem = false)
    )

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<PhotoModel>) {
        if (newData.isNotEmpty()) {
            data.clear()
//            data.add(PhotoModel(isNormalItem = false))

//            val oldSize = itemCount
//            if (newData.size + oldSize > 5) {
//                data.removeAt(oldSize - 1)
                data.addAll(newData)
//            } else {
//                if (oldSize == 0) {
//                    data.addAll(0, newData)
//                } else {
//                    data.addAll(oldSize - 1, newData)
//                }
//            }
            notifyDataSetChanged()
        }
    }

    fun setClickListener(listener: OnPickPhotoItemClickListener) {
        onPickPhotoItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemBinding = ItemReadDiaryLoveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PickingPhotoViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class PickingPhotoViewHolder(private val binding: ItemReadDiaryLoveBinding) :
        BaseViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        override fun onBind(position: Int) {
            binding.apply {
                photoModel = data[position]
                root.clickWithDebounce {
//                    if (!data[position].isNormalItem) {
                        onPickPhotoItemClickListener!!.onClickItemRootClick(position,data)
//                    }
                }
            }
        }
    }

    interface OnPickPhotoItemClickListener {
        fun onClickItemRootClick(position: Int,imageList: MutableList<PhotoModel>)
    }

}