package com.example.keepfitfinal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DailyGoal::class, DailyAchievedGoal::class], version = 1)
@TypeConverters(Converters::class)
abstract class KeepFitDatabase : RoomDatabase() {

    abstract fun dailyGoal() : DailyGoalDao
    abstract fun achievedGoal() : AchievedGoalDao

    companion object {

        private var INSTANCE: KeepFitDatabase? = null

        fun getInstance(context: Context): KeepFitDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, KeepFitDatabase::class.java, "FitApp")
                    .build()
            }
            return INSTANCE as KeepFitDatabase
        }
    }

}