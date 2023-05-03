package com.example.app_my_diary.ui.event.dialog.pickphoto

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.app_my_diary.base.BaseViewHolder
import com.example.app_my_diary.databinding.ItemAlbumBinding
import com.example.app_my_diary.databinding.ItemPickPhotoBinding
import com.example.app_my_diary.model.PhotoModel
import com.example.app_my_diary.model.PickPhotoModel
import com.example.app_my_diary.model.PickPhotoType
import com.example.app_my_diary.utils.Constants
import com.example.app_my_diary.utils.Utils
import com.example.app_my_diary.utils.clickWithDebounce
import java.util.function.Predicate

class PickPhotoStoryAdapter(val isEdit: Boolean) :
    RecyclerView.Adapter<BaseViewHolder>() {

    var pickPhotoModel: PickPhotoModel? = null
    var listener: ListenerClickItem? = null
    var pickList = mutableListOf<PhotoModel>()
    var maximum = Constants.STORY_IMAGE_MAXIMUM

    @SuppressLint("NotifyDataSetChanged")
    fun update(pickPhotoModel: PickPhotoModel?) {
        this.pickPhotoModel = pickPhotoModel
        notifyDataSetChanged()
    }

    fun updatePickList(list: List<PhotoModel>) {
        if (list.isNotEmpty()) {
            pickList.addAll(list)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == PickPhotoType.Album.ordinal) {
            val itemBinding =
                ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AlbumViewHolder(itemBinding)
        } else {
            val itemBinding =
                ItemPickPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            PickPhotoViewHolder(itemBinding)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = if (pickPhotoModel == null) {
        0
    } else {
        pickPhotoModel!!.photoModelList.size
    }

    inner class PickPhotoViewHolder(val binding: ItemPickPhotoBinding) :
        BaseViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onBind(position: Int) {
            binding.apply {
                val model = pickPhotoModel!!.photoModelList[position]
                pickList.forEachIndexed { index, photoModel ->
                    if (photoModel.uriString == model.uriString) {
                        model.apply {
                            isSelected = photoModel.isSelected
                            count = index + 1
                        }
                    }
                }
                photoModel = model
                root.clickWithDebounce (Constants.CLICK_DELAY){
                    if (Utils.checkImage(model.uri, root.context)) {
                        if (position < itemCount) {
                            if (pickList.size < maximum || model.isSelected) {
                                model.isSelected = !model.isSelected
                                if (model.isSelected) {
                                    if (isEdit) {
                                        model.apply {
                                            isAddMore = true
                                            count = pickList.size + 1
                                        }
                                    } else {
                                        model.count = pickList.size + 1
                                    }
                                    pickList.add(model)
                                } else {
                                    if (model.count < pickList.size) {
                                        pickPhotoModel!!.photoModelList.forEachIndexed { index, photoModel ->
                                            if (photoModel.count > model.count) {
                                                photoModel.count -= 1
                                                notifyItemChanged(index)
                                            }
                                        }
                                    }
                                    val predicate =
                                        Predicate { photoModel: PhotoModel -> photoModel.uriString == model.uriString }
                                    pickList.removeIf(predicate)
                                }
                                notifyItemChanged(position)
                                listener?.onPickPhoto(
                                    model,
                                    model.count,
                                    model.isSelected,
                                    pickList.size
                                )
                            } else {
                                listener?.onMaximumPhoto()
                            }
                        }
                    } else {
                        listener?.onImageError()
                    }
                }
            }
        }
    }

    inner class AlbumViewHolder(val binding: ItemAlbumBinding) : BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.photoModel = pickPhotoModel!!.photoModelList[position]
            binding.myLayoutRoot.setOnClickListener {
                if (position < itemCount) {
                    listener?.onClickFileItem(
                        position,
                        pickPhotoModel!!.pickPhotoType,
                        pickPhotoModel!!.photoModelList[position]
                    )
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (pickPhotoModel != null) {
            pickPhotoModel!!.pickPhotoType.ordinal
        } else {
            PickPhotoType.Photo.ordinal
        }
    }

    interface ListenerClickItem {
        fun onClickFileItem(
            position: Int,
            pickPhotoType: PickPhotoType,
            photoModel: PhotoModel
        )

        fun onPickPhoto(
            photoModel: PhotoModel,
            count: Int,
            isAdd: Boolean,
            size: Int
        )

        fun onMaximumPhoto()

        fun onImageError()
    }
}