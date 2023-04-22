package com.example.keepfitfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepfitfinal.adapter.DailyGoalAdapter
import com.example.keepfitfinal.database.KeepFitDatabase
import com.example.keepfitfinal.databinding.FragmentGoalmgmtFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class GoalmgmtFragment : Fragment() {

    lateinit var binding: FragmentGoalmgmtFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGoalmgmtFragmentBinding.inflate(inflater)
        initView()
        fetchDataBaseDetails()
        return binding.root
    }

    private fun initView() {

        binding.recyclerDailyGoal.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAddNewGoal.setOnClickListener {
            findNavController().navigate(R.id.action_nav_goalmgmt_to_addgoal)
        }
    }

    private fun fetchDataBaseDetails() {
        GlobalScope.launch(Dispatchers.IO) {
            val dailyGoalDao = KeepFitDatabase.getInstance(requireContext()).dailyGoal()
            val recored = dailyGoalDao.getAllGoals()
            withContext(Dispatchers.Main) {
                binding.recyclerDailyGoal.adapter = DailyGoalAdapter(this@GoalmgmtFragment, recored.toCollection(ArrayList()))
            }
        }
    }


}
