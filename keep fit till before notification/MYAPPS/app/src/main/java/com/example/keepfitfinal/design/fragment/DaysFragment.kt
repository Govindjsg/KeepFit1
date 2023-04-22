package com.example.keepfitfinal.design.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.keepfitfinal.R
import com.example.keepfitfinal.Utils
import com.example.keepfitfinal.database.DailyAchievedGoal
import com.example.keepfitfinal.database.DailyGoal
import com.example.keepfitfinal.database.KeepFitDatabase
import com.example.keepfitfinal.databinding.FragmentDayStatusBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class DaysFragment : Fragment() {

    lateinit var binding: FragmentDayStatusBinding

    private var currentActiveGoal: DailyGoal? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_day_status, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        binding.btnsetDgoal.setOnClickListener {
            findNavController().navigate(R.id.action_nav_dayst_to_nav_goalmgmt)
        }
        binding.progressBar.percent = 0f
        getTodayGoal()

        binding.btnAddProgress.setOnClickListener {
            Utils.hideKeyboard(it)
            val steps = binding.edtNoOgSteps.text.toString()
            if (steps.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter valid Steps", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (currentActiveGoal == null) {

                AlertDialog.Builder(requireContext()).setTitle("Goal Alert!")
                    .setMessage("Currently you have not active Goal, Please select the goal first")
                    .setPositiveButton("Go to select goal") { _, _ ->
                        findNavController().navigate(R.id.action_nav_dayst_to_nav_goalmgmt)
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .show()
                return@setOnClickListener
            }
            AlertDialog.Builder(requireContext()).setTitle("Add Steps")
                .setMessage("Are you sure want to add steps $steps")
                .setPositiveButton("Add Steps") { _, _ ->
                    setProgress(steps.toInt())
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .show()
        }
    }

    private fun setProgress(step: Int) {
        try {

            val achGoal = DailyAchievedGoal(null, Utils.getCurrentDate(), step, currentActiveGoal?.steps ?: 0)
            GlobalScope.launch(Dispatchers.IO) {
                KeepFitDatabase.getInstance(requireContext()).achievedGoal().addNewAchievement(achGoal)
                withContext(Dispatchers.Main) {
                    binding.edtNoOgSteps.setText("")
                    getTodayGoal()
                }
            }
        } catch (e: java.lang.Exception) {
        }
    }

    private fun getTodayGoal() {
        GlobalScope.launch(Dispatchers.IO) {
            val startTime = Utils.getCurrentDate()
            startTime.set(Calendar.SECOND, 0)
            startTime.set(Calendar.HOUR, 0)
            startTime.set(Calendar.MINUTE, 0)

            val endTime = Utils.getCurrentDate()
            endTime.set(Calendar.HOUR, 23)
            endTime.set(Calendar.SECOND, 59)
            endTime.set(Calendar.MINUTE, 59)

            val db = KeepFitDatabase.getInstance(requireContext())
            val activeGoal = db.dailyGoal().getActiveGoal()
            val todayAchievement = db.achievedGoal().getAllAchievementByDate(startTime, endTime)

            withContext(Dispatchers.Main) {
                setRecords(activeGoal, todayAchievement)
            }

        }
    }

    private fun setRecords(activeGoal: DailyGoal?, todayAchievement: List<DailyAchievedGoal>) {
        var totalAchievement = 0
        currentActiveGoal = activeGoal

        binding.txtGoal.text = "${getString(R.string.todaysgl)} ${activeGoal?.steps}"
        todayAchievement.forEach {
            totalAchievement += (it.steps ?: 0)
        }
        if (activeGoal == null) {
            binding.btnsetDgoal.visibility = View.VISIBLE
            return
        } else {
            binding.btnsetDgoal.visibility = View.GONE
        }
        try {
            var achPer = (totalAchievement.toFloat() * 100) / (activeGoal.steps ?: 0)
            if (achPer > 100) {
                achPer = 100f
            }
            binding.progressBar.percent = achPer
            binding.progressLine.progress = achPer.toInt()
        } catch (e: Exception) {

        }
    }
}