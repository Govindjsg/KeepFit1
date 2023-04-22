package com.example.keepfitfinal.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keepfitfinal.R
import com.example.keepfitfinal.Utils
import com.example.keepfitfinal.database.DailyAchievedGoal

class HistoryAdapter(var achivedGoal: ArrayList<DailyAchievedGoal>) : RecyclerView.Adapter<HistoryAdapter.Holder>() {

    override fun getItemCount() = achivedGoal.size


    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        var tvDays: TextView = view.findViewById(R.id.tvDays)
        var tvSteps: TextView = view.findViewById(R.id.tvSteps)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_history, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = achivedGoal[position]
        holder.tvDays.text = Utils.getFormattedDate(item.date)
        holder.tvSteps.text = item.steps.toString()
    }
}