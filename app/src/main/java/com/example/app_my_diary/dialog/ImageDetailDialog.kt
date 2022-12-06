package com.example.app_my_diary.dialog

import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.view.doOnLayout
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseFullAlertDialog
import com.example.app_my_diary.databinding.DialogStoryDetailBinding
import com.example.app_my_diary.model.PhotoModel
import com.example.app_my_diary.utils.clickWithDebounce
import com.example.app_my_diary.viewpager.ViewPagerAdapter

class ImageDetailDialog : BaseFullAlertDialog<DialogStoryDetailBinding>() {

    private var currentPosition: Int? = null
    private var imageList: MutableList<PhotoModel>? = null
    private val mAdapter = ViewPagerAdapter()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        dialog!!.window!!.setDimAmount(0.8F)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPosition = requireArguments().getInt(KEY_CURRENT_POSITION)
        imageList = requireArguments().getParcelableArrayList(KEY_IMAGE_LIST)
    }

    override fun initView() {
        binding!!.apply {
            mAdapter.updateData(imageList!!)
            vpImage.apply {
                adapter = mAdapter
                offscreenPageLimit = 1
                doOnLayout {
                    setCurrentItem(currentPosition!!, true)
                }
            }
            btClose.clickWithDebounce {
                dismiss()
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.dialog_story_detail
    }

    companion object {
        private const val KEY_CURRENT_POSITION = "Key_Current_Position"
        private const val KEY_IMAGE_LIST = "Key_Image_List"

        fun create(position: Int, imageList: MutableList<PhotoModel>): ImageDetailDialog {
            val dialog = ImageDetailDialog()
            val bundle = Bundle()
            bundle.putInt(KEY_CURRENT_POSITION, position)
            bundle.putParcelableArrayList(KEY_IMAGE_LIST, ArrayList(imageList))
            dialog.arguments = bundle
            return dialog
        }
    }
}