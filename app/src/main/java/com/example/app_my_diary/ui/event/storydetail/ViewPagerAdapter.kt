package com.example.app_my_diary.ui.event.storydetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_my_diary.base.BaseViewHolder
import com.example.app_my_diary.databinding.ItemStoryDetailBinding
import com.example.app_my_diary.model.PhotoModel

class ViewPagerAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    val data = mutableListOf<PhotoModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list : MutableList<PhotoModel>){
        if(list.isNotEmpty()){
            data.clear()
            data.addAll(list)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemBinding =
            ItemStoryDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class PhotoViewHolder(val binding: ItemStoryDetailBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.apply {
                photoModel = data[position]
            }
        }
    }
}