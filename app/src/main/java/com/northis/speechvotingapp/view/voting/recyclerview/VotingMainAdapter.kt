package com.northis.speechvotingapp.view.voting.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.model.Voting
import com.northis.speechvotingapp.viewmodel.VotingViewModel
import java.util.*
import kotlin.collections.ArrayList

class VotingMainAdapter(
    private val context: Context?,
    private val votingList: ArrayList<Voting>,
    private val navController: NavController,
    private val vm: VotingViewModel
) : RecyclerView.Adapter<VotingMainAdapter.MainFragmentViewHolder>() {
    class MainFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val titleTV: TextView by lazy { itemView.findViewById(R.id.voting_title) }
        internal val daysLeftTV: TextView by lazy { itemView.findViewById(R.id.voting_days_left) }
        internal val votedUsersTotalTV: TextView by lazy { itemView.findViewById(R.id.voting_voted_users_total) }
        internal val holderLayout: ConstraintLayout by lazy { itemView.findViewById(R.id.main_fragment_view_holder) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.main_fragment_view_holder, parent, false)
        return MainFragmentViewHolder(view)

    }

    override fun onBindViewHolder(holder: MainFragmentViewHolder, position: Int) {
        val voting = votingList[position]
        holder.titleTV.text = voting.Title
        holder.votedUsersTotalTV.text = "${voting.TotalVotes}"
        holder.daysLeftTV.text = getTimeHasLeft(voting)
            holder.holderLayout.setOnClickListener {
                vm.votingId = voting.VotingId.toString()
                navController.navigate(R.id.action_votingMainFragment_to_votingDetailsFragment)
            }
    }

    override fun getItemCount(): Int {
        return votingList.size
    }

    private fun getTimeHasLeft(voting: Voting): String {
        val endDate = voting.EndDate?.time ?: 0
        val currentDate = Date().time
        val result = endDate - currentDate
        if (result < 0) return "0"
        // TODO CHANGE TO CALENDAR
        if (result > 0) return "${voting.EndDate?.day}"
        return ""
    }
}