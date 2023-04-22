package com.example.keepfitfinal.adapter


import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfitfinal.R
import com.example.keepfitfinal.database.DailyGoal
import com.example.keepfitfinal.database.KeepFitDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DailyGoalAdapter(var currentFragment: Fragment, var dailyGoals: ArrayList<DailyGoal>) : RecyclerView.Adapter<DailyGoalAdapter.Holder>() {

    override fun getItemCount() = dailyGoals.size


    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        var txtGoalName: TextView = view.findViewById(R.id.txtGoalName)
        var txtGoalStep: TextView = view.findViewById(R.id.txtGoalStep)
        var btnSetGoal: TextView = view.findViewById(R.id.btnSetGoal)
        var btnEditGoal: ImageView = view.findViewById(R.id.btnEditGoal)
        var btnDeleteGoal: ImageView = view.findViewById(R.id.btnDeleteGoal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_daily_goal, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = dailyGoals[position]
        holder.txtGoalStep.text = item.steps.toString()
        holder.txtGoalName.text = item.name
        if (item.isSelected == true) {
            holder.btnSetGoal.isEnabled = false
            holder.btnSetGoal.background = ContextCompat.getDrawable(
                currentFragment.requireContext(), R.drawable.background_gray
            )
        } else {
            holder.btnSetGoal.isEnabled = true
            holder.btnSetGoal.background = ContextCompat.getDrawable(
                currentFragment.requireContext(), R.drawable.background_primary
            )
        }

        holder.btnSetGoal.setOnClickListener {
            dailyGoals.forEach {
                it.isSelected = false
                if (it.id == item.id) {
                    it.isSelected = true
                }
            }
            storeDataIntoDatabase(currentFragment.requireContext())
        }
        holder.btnEditGoal.setOnClickListener {
            val bund = bundleOf(Pair("edit", item.id))
            currentFragment.findNavController().navigate(R.id.action_nav_goalmgmt_to_addgoal, bund)
        }
        holder.btnDeleteGoal.setOnClickListener {
            if (item.isSelected == true) {

                val dialog = AlertDialog.Builder(currentFragment.requireContext()).setTitle("Alert!")
                    .setMessage("You can not delete the active goal, you can delete rather than this  \"${item.name}\" Goal.")
                    .setPositiveButton("okay") { d, _ ->
                        d.dismiss()
                    }
                dialog.show()

                return@setOnClickListener
            }

            val dialog = AlertDialog.Builder(currentFragment.requireContext()).setTitle("Delete!")
                .setMessage("Are you sure want to delete \"${item.name}\"?")
                .setPositiveButton("Delete") { _, _ ->
                    removeGoalFromList(item)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
        }
    }

    private fun removeGoalFromList(item: DailyGoal) {

        GlobalScope.launch(Dispatchers.IO) {
            val database = KeepFitDatabase.getInstance(currentFragment.requireContext()).dailyGoal()
            database.deleteRecord(item)
            dailyGoals.remove(item)
            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }
    }

    private fun storeDataIntoDatabase(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            val database = KeepFitDatabase.getInstance(context).dailyGoal()
            database.storeGoal(*dailyGoals.toTypedArray())
            withContext(Dispatchers.Main) {
                notifyDataSetChanged()
            }
        }

    }

}