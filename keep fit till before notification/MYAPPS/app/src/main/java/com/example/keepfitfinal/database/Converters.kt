package com.example.keepfitfinal.database

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun toDate(date: Long): Date {
        return Date(date)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long {
        return date?.time ?: Calendar.getInstance().timeInMillis
    }

    @TypeConverter
    fun toCalendar(l: Long?): Calendar? =
        if (l == null) null else Calendar.getInstance().apply { timeInMillis = l }

    @TypeConverter
    fun fromCalendar(c: Calendar?): Long? = c?.time?.time
}