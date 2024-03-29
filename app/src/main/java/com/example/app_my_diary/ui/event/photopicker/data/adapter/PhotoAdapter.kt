package com.example.app_my_diary.ui.event.photopicker.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_my_diary.base.BaseViewHolder
import com.example.app_my_diary.databinding.ItemAlbumBinding
import com.example.app_my_diary.databinding.ItemPhotoBinding
import com.example.app_my_diary.model.PhotoModel
import com.example.app_my_diary.model.PickPhotoModel
import com.example.app_my_diary.model.PickPhotoType

class PhotoAdapter(
    private val listener: ListenerClickItem
) :
    RecyclerView.Adapter<BaseViewHolder>() {

    var pickPhotoModel: PickPhotoModel? = null

    @SuppressLint("NotifyDataSetChanged")
    fun update(pickPhotoModel: PickPhotoModel?) {
        this.pickPhotoModel = pickPhotoModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == PickPhotoType.Album.ordinal) {
            val itemBinding = ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            AlbumViewHolder(itemBinding)
        } else {
            val itemBinding = ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            PhotoViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int = if (pickPhotoModel == null) {
        0
    } else {
        pickPhotoModel!!.photoModelList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class AlbumViewHolder(val binding: ItemAlbumBinding) : BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.photoModel = pickPhotoModel!!.photoModelList[position]
            binding.myLayoutRoot.setOnClickListener {
                if (position < itemCount) {
                    listener.onClickFileItem(
                        position,
                        pickPhotoModel!!.pickPhotoType,
                        pickPhotoModel!!.photoModelList[position]
                    )
                }
            }
        }
    }

    inner class PhotoViewHolder(val binding: ItemPhotoBinding) : BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.photoModel = pickPhotoModel!!.photoModelList[position]
            binding.myLayoutRoot.setOnClickListener {
                if (position < itemCount) {
                    listener.onClickFileItem(
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
    }
}
