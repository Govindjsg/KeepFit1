package com.example.keepfitfinal

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.*

object Utils {

    fun getFormattedDate(date: Calendar?): String {
        return "${date?.get(Calendar.DATE)}/${date?.get(Calendar.MONTH)}/${date?.get(Calendar.YEAR)}"
    }

    fun getCurrentDate(): Calendar {
        return Calendar.getInstance()
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}