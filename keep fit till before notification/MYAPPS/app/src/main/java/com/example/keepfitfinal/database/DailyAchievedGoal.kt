package com.example.keepfitfinal.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "achieved_goal")
data class DailyAchievedGoal(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    var date: Calendar?,
    var steps: Int?,
    var fromTarget: Int
)