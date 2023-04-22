package com.example.keepfitfinal.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import java.util.*

@Dao
interface AchievedGoalDao {

    @Query("SELECT * FROM achieved_goal  ORDER BY date DESC")
    fun getAchievementGoals(): List<DailyAchievedGoal>

    @Query("SELECT * FROM achieved_goal WHERE date > :toDate AND date < :fromDate ORDER BY date DESC")
    fun getAllAchievementByDate(toDate: Calendar, fromDate: Calendar): List<DailyAchievedGoal>

    @Delete
    fun deleteRecord(goal: DailyAchievedGoal)

    @Insert(onConflict = REPLACE)
    fun storeAchievementGoal(vararg stocks: DailyAchievedGoal)

    @Insert(onConflict = REPLACE)
    fun addNewAchievement(stocks: DailyAchievedGoal): Long

    @Query("SELECT * FROM achieved_goal WHERE id =:id")
    fun getAchievementByID(id: Int): DailyAchievedGoal

    @Query("DELETE FROM achieved_goal")
    fun deleteHistory()
}