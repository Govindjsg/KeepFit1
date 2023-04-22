package com.example.keepfitfinal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.keepfitfinal.database.DailyGoal
import com.example.keepfitfinal.database.KeepFitDatabase
import com.example.keepfitfinal.databinding.FragmentAddgoalFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class AddGoalFragment : Fragment() {

    lateinit var binding: FragmentAddgoalFragmentBinding

    private var updateGoal: DailyGoal? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddgoalFragmentBinding.inflate(inflater)
        initView()
        checkForUpdate()
        return binding.root// inflater.inflate(R.layout.fragment_addgoal_fragment, container, false)
    }

    private fun checkForUpdate() {
        arguments?.let { arg ->
            if (arg.containsKey("edit")) {
                val id = arg.getInt("edit")
                GlobalScope.launch(Dispatchers.IO) {
                    val database = KeepFitDatabase.getInstance(requireContext()).dailyGoal()
                    updateGoal = database.getGoalByID(id)
                    withContext(Dispatchers.Main) {
                        binding.edtGoalSteps.setText((updateGoal?.steps ?: 0).toString())
                        binding.edtGoalName.setText(updateGoal?.name ?: "")
                        binding.btnAddGoal.text = "Apply Changes"
                    }
                }
            }

        }
    }

    private fun initView() {
        binding.btnAddGoal.setOnClickListener {
            addNewGoal()
        }
    }

    private fun addNewGoal() {
        try {
            val goalName = binding.edtGoalName.text.toString()
            val goalSteps = binding.edtGoalSteps.text.toString()
            val foramtedDate = Utils.getCurrentDate()

            if (goalName.isEmpty()) {
                binding.edtGoalName.setError("Enter valid Goal name")
                binding.edtGoalName.requestFocus()
            } else if (goalSteps.isEmpty()) {
                binding.edtGoalSteps.setError("Enter valid Goal Steps")
                binding.edtGoalSteps.requestFocus()
            } else {
                val dailGoal: DailyGoal = if (updateGoal == null) {
                    DailyGoal(null, goalName, goalSteps.toInt(), false, foramtedDate)
                } else {

                    updateGoal?.name = goalName
                    updateGoal?.steps = goalSteps.toInt()
                    updateGoal!!
                }

                GlobalScope.launch {
                    val database = KeepFitDatabase.getInstance(requireContext()).dailyGoal()
                    val result = database.storeSingleGoal(dailGoal)
                    if (result > -1) {
                        goalAdded()
                    }

                }

            }
        } catch (e: Exception) {

        }


    }

    private fun goalAdded() {
        requireActivity().runOnUiThread {
            findNavController().popBackStack()
            Toast.makeText(requireContext(), "Goal Added Successfully", Toast.LENGTH_LONG).show()
        }
    }
}