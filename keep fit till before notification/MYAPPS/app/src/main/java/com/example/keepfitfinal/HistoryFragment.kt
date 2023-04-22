package com.example.keepfitfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keepfitfinal.adapter.HistoryAdapter
import com.example.keepfitfinal.database.KeepFitDatabase
import com.example.keepfitfinal.databinding.FragmentHistoryFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HistoryFragment : Fragment() {

    lateinit var binding: FragmentHistoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryFragmentBinding.inflate(inflater)
        initView()
        return binding.root
    }

    private fun initView() {
        val isShowHistory = AppPreference(requireContext()).getBooleanPreference(AppPreference.IS_HISTRORY_ENABLE)
        if (!isShowHistory) {
            binding.rlEnableHistory.visibility = View.VISIBLE
            binding.recyclerHistory.visibility = View.GONE
            binding.tvClearHistory.visibility = View.GONE
        } else {
            binding.rlEnableHistory.visibility = View.GONE
            binding.recyclerHistory.visibility = View.VISIBLE
            binding.tvClearHistory.visibility = View.VISIBLE
        }

        binding.recyclerHistory.layoutManager = LinearLayoutManager(requireContext())

        binding.btnEnableHistory.setOnClickListener {
            AppPreference(requireContext()).setBooleanPreference(AppPreference.IS_HISTRORY_ENABLE, true)
            initView()
        }

        binding.tvClearHistory.setOnClickListener {

            AlertDialog.Builder(requireContext()).setTitle("Alert")
                .setMessage("Are you sure want to remove history? This will loos your all data")
                .setPositiveButton("Clear History") { _, _ ->
                    clearHistory()
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .show()

        }

        binding.btnGoBackHistory.setOnClickListener {
            findNavController().popBackStack()
        }

        if (isShowHistory) {
            getHistoryDetails()
        }

    }

    private fun clearHistory() {
        GlobalScope.launch(Dispatchers.IO) {
            KeepFitDatabase.getInstance(requireContext()).achievedGoal().deleteHistory()
            withContext(Dispatchers.Main) {
                binding.recyclerHistory.adapter = HistoryAdapter(ArrayList())

            }
        }
    }

    private fun getHistoryDetails() {
        GlobalScope.launch(Dispatchers.IO) {
            var data = KeepFitDatabase.getInstance(requireContext()).achievedGoal().getAchievementGoals()
            withContext(Dispatchers.Main) {
                if (data.isNotEmpty()) {
                    binding.recyclerHistory.adapter = HistoryAdapter(data.toCollection(ArrayList()))
                }
            }
        }
    }


}