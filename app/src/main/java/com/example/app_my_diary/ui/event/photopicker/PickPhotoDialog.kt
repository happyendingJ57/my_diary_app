package com.example.app_my_diary.ui.event.photopicker

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.app_my_diary.R
import com.example.app_my_diary.app.App
import com.example.app_my_diary.base.BaseFullAlertDialog
import com.example.app_my_diary.databinding.DialogPickPhotoBinding
import com.example.app_my_diary.model.PhotoModel
import com.example.app_my_diary.model.PickPhotoType
import com.example.app_my_diary.ui.event.photopicker.data.adapter.PhotoAdapter
import com.example.app_my_diary.utils.DataResponse
import com.example.app_my_diary.utils.LoadDataStatus
import com.example.app_my_diary.utils.ToastUtils
import com.example.app_my_diary.utils.Utils
import com.example.app_my_diary.viewmodel.PickPhotoViewModel


class PickPhotoDialog : BaseFullAlertDialog<DialogPickPhotoBinding>(),
    PhotoAdapter.ListenerClickItem {
    private lateinit var mViewModel: PickPhotoViewModel
    private lateinit var mAdapter: PhotoAdapter
    var onPhotoPickListener: OnPhotoPickListener? = null
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var app: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen)
        app = requireActivity().application as App
        initViewModel()
        mAdapter = PhotoAdapter(this)
    }

    override fun getLayout(): Int {
        return R.layout.dialog_pick_photo
    }

    override fun initView() {
        binding!!.apply {
            recycleView.setHasFixedSize(true)
            recycleView.adapter = mAdapter
        }
        binding!!.viewModel = mViewModel
        mViewModel.loadingAll(-1L)
    }

    private fun onBackPress() {
        mViewModel.onClose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setOnKeyListener { dialog, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPress()
            }
            true
        }
    }

    private fun initViewModel() {
        val factory = PickPhotoViewModel.Factory(requireActivity().application as App)
        mViewModel = ViewModelProvider(this, factory)[PickPhotoViewModel::class.java]
        mViewModel.mPickPhotoModel.observe(this) {
            it?.let {
                if (it.loadDataStatus == LoadDataStatus.SUCCESS) {
                    val body = (it as DataResponse.DataSuccessResponse).body
                    mLayoutManager = GridLayoutManager(
                        requireActivity(), if (body.pickPhotoType == PickPhotoType.Photo) {
                            app.photoColumns + 1
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


    override fun onClickFileItem(
        position: Int, pickPhotoType: PickPhotoType, photoModel: PhotoModel
    ) {
        if (pickPhotoType == PickPhotoType.Photo) {
            if (Utils.checkImage(photoModel.uri, mainActivity)) {
                dismiss()
                onPhotoPickListener?.onPicked(photoModel)
            } else {
                ToastUtils.getInstance(mainActivity)
                    .showToast(resources.getString(R.string.image_error))
            }
        } else {
            binding!!.tvAlbumName.text = photoModel.albumName
            mViewModel.loadingAll(photoModel.albumId)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    interface OnPhotoPickListener {
        fun onPicked(photoModel: PhotoModel?)
    }

    companion object {
        fun create(): PickPhotoDialog {
            return PickPhotoDialog()
        }
    }
}