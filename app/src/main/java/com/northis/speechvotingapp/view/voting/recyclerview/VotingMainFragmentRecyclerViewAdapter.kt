package com.northis.speechvotingapp.view.voting.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.model.Voting

class VotingMainFragmentRecyclerViewAdapter(
    private val context: Context?,
    private val votingList: List<Voting>,
    private val navController: NavController
) :
    RecyclerView.Adapter<VotingMainFragmentRecyclerViewAdapter.MainFragmentViewHolder>() {
    class MainFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val titleTV: TextView by lazy { itemView.findViewById(R.id.voting_title) }
        internal val daysLeftTV: TextView by lazy { itemView.findViewById(R.id.voting_days_left) }
        internal val votedUsersTotalTV: TextView by lazy { itemView.findViewById(R.id.voting_voted_users_total) }
        internal val mainLayout: ConstraintLayout by lazy { itemView.findViewById(R.id.main_fragment_view_holder) }
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
        holder.daysLeftTV.text = "0"
        holder.mainLayout.setOnClickListener {
            val bundle = bundleOf("votingId" to voting.VotingId.toString())
            navController.navigate(R.id.action_votingMainFragment_to_votingDetailsFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return votingList.size
    }
}