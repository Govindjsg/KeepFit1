package com.example.keepfitfinal.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "daily_goal")
data class DailyGoal(
    @PrimaryKey (autoGenerate = true) var id: Int?,
    var name: String?,
    var steps: Int?,
    var isSelected: Boolean?,
    var date: Calendar?
)