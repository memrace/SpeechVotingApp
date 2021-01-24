package com.northis.speechvotingapp.view.voting.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.model.Voting
import com.northis.speechvotingapp.viewmodel.VotingViewModel

class VotingDetailsAdapter(
    private val context: Context?,
    private val voting: Voting,
    private val vm: VotingViewModel
) : RecyclerView.Adapter<VotingDetailsAdapter.DetailsFragmentViewHolder>() {
    class DetailsFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val speechTheme: TextView by lazy { itemView.findViewById(R.id.speechTheme) }
        internal val speechCreator: TextView by lazy { itemView.findViewById(R.id.speechCreator) }
        internal val holderLayout: ConstraintLayout by lazy { itemView.findViewById(R.id.details_fragment_view_holder) }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsFragmentViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.details_fragment_view_holder, parent, false)
        return DetailsFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailsFragmentViewHolder, position: Int) {
        val speech = voting.VotingSpeeches?.get(position)?.Speech
        holder.speechTheme.text = speech?.Theme
        holder.speechCreator.text = "${speech?.Creator?.FirstName} ${speech?.Creator?.LastName}"
        holder.holderLayout.setOnClickListener {
            vm.speechVotingId.value = speech?.SpeechId.toString()
        }
    }

    override fun getItemCount(): Int {
        return voting.VotingSpeeches?.size as Int
    }

}