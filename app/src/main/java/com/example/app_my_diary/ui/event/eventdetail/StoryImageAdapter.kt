package com.example.app_my_diary.ui.event.eventdetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_my_diary.base.BaseViewHolder
import com.example.app_my_diary.databinding.ItemStoryImageBinding
import com.example.app_my_diary.model.PhotoModel
import com.example.app_my_diary.utils.clickWithDebounce

class StoryImageAdapter(private val columns: Int, val isDetail: Boolean) :
    RecyclerView.Adapter<BaseViewHolder>() {

    val imageList = mutableListOf<PhotoModel>()
    var listener: OnPhotoStoryClickListener? = null
        set(value) {
            field = value
        }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: List<PhotoModel>) {
        imageList.clear()
        imageList.addAll(list)
        notifyDataSetChanged()
    }

    inner class StoryImageViewHolder(val binding: ItemStoryImageBinding) :
        BaseViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun onBind(position: Int) {
            binding.apply {
                photoModel = imageList[position]
                if (!isDetail) {
                    if (position == columns - 1 && imageList.size > columns) {
                        llOverlay.visibility = View.VISIBLE
                        tvCount.text = "+${imageList.size - columns}"
                    }
                }

                root.clickWithDebounce {
                    listener?.onPhotoStoryClickListener(position, imageList)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemBinding =
            ItemStoryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryImageViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (isDetail) {
            holder.onBind(position)
        } else {
            if (position < columns) {
                holder.onBind(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (isDetail) {
            imageList.size
        } else {
            if (imageList.size < columns) {
                imageList.size
            } else {
                columns
            }
        }
    }

    interface OnPhotoStoryClickListener {
        fun onPhotoStoryClickListener(position: Int, imageList: MutableList<PhotoModel>)
    }
}