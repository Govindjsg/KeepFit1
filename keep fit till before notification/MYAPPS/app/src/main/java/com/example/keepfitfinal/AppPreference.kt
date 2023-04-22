package com.example.keepfitfinal

import android.content.Context
import android.content.SharedPreferences

class AppPreference(var context: Context) {
    companion object {
        val IS_HISTRORY_ENABLE = "isHistoryEnable"
    }

    val PREF_NAME = "KeepFit"
    private lateinit var mPreferences: SharedPreferences

    init {
        setPreference()
    }

    private fun setPreference() {
        mPreferences = context.getSharedPreferences(
            PREF_NAME, Context.MODE_PRIVATE
        )
    }

    fun setBooleanPreference(keyName: String, value: Boolean) {
        val editor = mPreferences.edit()
        editor.putBoolean(keyName, value)
        editor.apply()
        editor.commit()
    }

    fun getBooleanPreference(keyName: String) :Boolean{
       return mPreferences.getBoolean(keyName, false)
    }
}