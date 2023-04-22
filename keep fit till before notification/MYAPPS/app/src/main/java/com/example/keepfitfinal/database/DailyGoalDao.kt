package com.example.keepfitfinal.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface DailyGoalDao {

    @Query("SELECT * FROM daily_goal")
    fun getAllGoals(): List<DailyGoal>

    @Query("SELECT * FROM daily_goal WHERE date = :date")
    fun getAllGoalByDate(date: String): List<DailyGoal>

    @Query("SELECT * FROM daily_goal WHERE isSelected = 1")
    fun getActiveGoal():DailyGoal?

    @Delete
    fun deleteRecord(goal :DailyGoal)


    @Insert(onConflict = REPLACE)
    fun storeGoal(vararg stocks: DailyGoal)

    @Insert(onConflict = REPLACE)
    fun storeSingleGoal(stocks: DailyGoal): Long

    @Query("SELECT * FROM daily_goal WHERE id =:id")
    fun getGoalByID(id: Int): DailyGoal
}