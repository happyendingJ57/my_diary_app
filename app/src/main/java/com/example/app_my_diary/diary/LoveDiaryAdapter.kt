package com.example.app_my_diary.diary

import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_my_diary.base.BaseViewHolder
import com.example.app_my_diary.base.StoryImageAdapter
import com.example.app_my_diary.databinding.ItemDiaryBinding
import com.example.app_my_diary.model.DiaryModel
import com.example.app_my_diary.utils.Constants
import com.example.app_my_diary.utils.Utils
import com.example.app_my_diary.utils.clickWithDebounce

class LoveDiaryAdapter(private val app: Application) : RecyclerView.Adapter<BaseViewHolder>() {

    val data = mutableListOf<DiaryModel>()

    var imageLayoutManager: GridLayoutManager? = null

    var columns: Int = Constants.AT_LEAST_COLUMN

    private var _listener: OnItemClickListener? = null

    var imageStoryListener: StoryImageAdapter.OnPhotoStoryClickListener? = null
        set(value) {
            field = value
        }

    fun setLayoutManager(manager: GridLayoutManager) {
        imageLayoutManager = manager
    }

    fun setListener(listener: OnItemClickListener) {
        _listener = listener
    }

    fun setMaxColumns(columns: Int) {
        this.columns = columns
    }

    fun updateData(list: List<DiaryModel>) {
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

    inner class DiaryViewHolder(val binding: ItemDiaryBinding) : BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.apply {
                diaryModel = data[position]
                rvImages.apply {
                    val mAdapter = StoryImageAdapter(columns, false)
                    mAdapter.listener = imageStoryListener
                    adapter = mAdapter
                    val mLayoutManager = object : GridLayoutManager(app, columns) {
                        override fun canScrollVertically(): Boolean {
                            return false
                        }
                    }
                    layoutManager = mLayoutManager
                    mAdapter.updateData(Utils.jsonToList(data[position].images))
                }
                ivMore.clickWithDebounce {
                    _listener!!.onMoreIconClick(ivMore, data[position])
                }
                root.clickWithDebounce {
                    if(position == 0 && data.size != 1){
                        _listener?.onRootItemClick(data[position],1)
                    }else if(position == data.size - 1 && data.size != 1){
                        _listener?.onRootItemClick(data[position],2)
                    } else if(data.size == 1){
                        _listener?.onRootItemClick(data[position],4)
                    }
                    else{
                        _listener?.onRootItemClick(data[position],3)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemDiaryBinding =
            ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiaryViewHolder(itemDiaryBinding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnItemClickListener {
        fun onMoreIconClick(view: View, diaryModel: DiaryModel)
        fun onRootItemClick(diaryModel: DiaryModel,status :Int)
    }
}