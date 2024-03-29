package com.example.app_my_diary.dialog

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app_my_diary.R
import com.example.app_my_diary.adapter.PickPhotoStoryAdapter
import com.example.app_my_diary.app.App
import com.example.app_my_diary.base.BasePickPhotoDialog
import com.example.app_my_diary.databinding.DialogPickPhotoStoryBinding
import com.example.app_my_diary.model.PhotoModel
import com.example.app_my_diary.model.PickPhotoType
import com.example.app_my_diary.utils.Constants.STORY_IMAGE_MAXIMUM
import com.example.app_my_diary.utils.DataResponse
import com.example.app_my_diary.utils.LoadDataStatus
import com.example.app_my_diary.utils.ToastUtils
import com.example.app_my_diary.utils.clickWithDebounce
import com.example.app_my_diary.utils.toast.BottomToastUtils
import com.example.app_my_diary.viewmodel.PickPhotoViewModel
import java.util.function.Predicate


class PickPhotoStoryDialog : BasePickPhotoDialog<DialogPickPhotoStoryBinding>(),
    PickPhotoStoryAdapter.ListenerClickItem {

    private lateinit var mAdapter: PickPhotoStoryAdapter
    private var imageList = mutableListOf<PhotoModel>()
    var listener: OnPickImageDoneListener? = null
        set(value) {
            field = value
        }
    private var max = STORY_IMAGE_MAXIMUM
    private var isEdit = false
    private lateinit var toastUtils: BottomToastUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isEdit = requireArguments().getBoolean(KEY_IS_EDIT)
        imageList.addAll(requireArguments().getParcelableArrayList(KEY_IMAGE_LIST)!!)
        if (isEdit) {
            imageList.forEach {
                if (!it.isAddMore) {
                    max--
                }
            }
        }
        mAdapter = PickPhotoStoryAdapter(isEdit)
        toastUtils = BottomToastUtils(mainActivity)
    }

    override fun getLayout(): Int {
        return R.layout.dialog_pick_photo_story
    }

    override fun initView() {
        binding!!.apply {
            mAdapter.listener = this@PickPhotoStoryDialog
            if (isEdit) {
                val temp = imageList.filter { it.isAddMore }.toMutableList()
                mAdapter.updatePickList(temp)
                mAdapter.maximum = max
            } else {
                mAdapter.updatePickList(imageList)
            }

            recycleView.apply {
                adapter = mAdapter
                setHasFixedSize(true)
            }
            viewModel = mViewModel
            maximum = max
            tvDone.apply {
                if (!isEdit) {
                    checkAddMore(imageList.size)
                } else {
                    var count = 0
                    imageList.forEach {
                        if (it.isAddMore) {
                            count++
                        }
                    }
                    checkAddMore(count)
                }

                clickWithDebounce {
                    listener?.onDoneClickListener(imageList)
                    dismiss()
                }
            }
        }
        mViewModel.loadingAll(-1L)
    }

    fun checkAddMore(count: Int) {
        binding!!.tvDone.apply {
            text = if (count == 0) {
                isEnabled = false
                resources.getString(R.string.action_done)
            } else {
                isEnabled = true
                "Hoàn thành(${count})"
            }
        }
    }

    override fun initViewModel() {
        val factory = PickPhotoViewModel.Factory(mainActivity.application as Application)
        mViewModel = ViewModelProvider(this, factory)[PickPhotoViewModel::class.java]
        mViewModel.mPickPhotoModel.observe(this) {
            it?.let {
                if (it.loadDataStatus == LoadDataStatus.SUCCESS) {
                    val body = (it as DataResponse.DataSuccessResponse).body
                    mLayoutManager = GridLayoutManager(
                        requireActivity(),
                        if (body.pickPhotoType == PickPhotoType.Photo) {
                            3
                        } else {
                            1
                        }
                    )
                    binding!!.recycleView.layoutManager = mLayoutManager
                    mAdapter.update(body)
                }
            }
        }
        mViewModel.isExitDialog.observe(this) {
            if (it) {
                dismiss()
            }
        }
    }

    companion object {
        private const val KEY_IMAGE_LIST = "Key_Image_List"
        private const val KEY_IS_EDIT = "Key_Is_Edit"

        fun create(
            listPhotoModel: List<PhotoModel>,
            isEdit: Boolean,
        ): PickPhotoStoryDialog {
            val dialog = PickPhotoStoryDialog()
            val bundle = Bundle()
            bundle.putBoolean(KEY_IS_EDIT, isEdit)
            bundle.putParcelableArrayList(KEY_IMAGE_LIST, ArrayList(listPhotoModel))
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onClickFileItem(
        position: Int,
        pickPhotoType: PickPhotoType,
        photoModel: PhotoModel
    ) {
        if (pickPhotoType == PickPhotoType.Album) {
            binding!!.tvAlbumName.text = photoModel.albumName
            mViewModel.loadingAll(photoModel.albumId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onPickPhoto(
        photoModel: PhotoModel,
        count: Int,
        isAdd: Boolean,
        size: Int
    ) {
        binding!!.apply {
            imageList.apply {
                val predicate =
                    Predicate { model: PhotoModel -> model.uriString == photoModel.uriString }
                if (isAdd) {
                    add(photoModel)
                } else {
                    removeIf(predicate)
                }
            }

            tvDone.apply {
                if (!isEdit) {
                    checkAddMore(size)
                } else {
                    var mCount = 0
                    imageList.forEach {
                        if (it.isAddMore) {
                            mCount++
                        }
                    }
                    checkAddMore(mCount)
                }
            }
        }
    }

    override fun onMaximumPhoto() {
        toastUtils.getInstance(requireContext())
        toastUtils.showToast(
            BottomToastUtils.MessageType.Warning,
            getString(R.string.title_warning),
            "Bạn có thể chọn tối đa $max ảnh"
        )
    }

    override fun onImageError() {
        ToastUtils.getInstance(requireContext())
            .showToast(resources.getString(R.string.image_error))
    }

    interface OnPickImageDoneListener {
        fun onDoneClickListener(imageList: MutableList<PhotoModel>)
    }
}