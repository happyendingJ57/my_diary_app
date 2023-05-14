package com.example.app_my_diary.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.app_my_diary.model.UserModel
import com.google.gson.Gson

class SharedPreferenceUtils private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getUserModel(): UserModel {
        val profileString = getStringValue("key.user_model")
        return if (profileString.isNullOrEmpty()) {
            UserModel()
        } else {
            Gson().fromJson(profileString, UserModel::class.java)
        }
    }

    fun setUserModel(profileModel: UserModel) {
        putStringValue(
            "key.user_model", Gson().toJson(profileModel)
        )
    }

    private fun putStringValue(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value).apply()
    }

    private fun getStringValue(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    companion object {
        const val PREFERENCE_NAME = "diary_app"
        private var instance: SharedPreferenceUtils? = null
        fun getInstance(context: Context): SharedPreferenceUtils {
            if (instance == null) {
                instance = SharedPreferenceUtils(context)
            }
            return instance!!
        }
    }

}