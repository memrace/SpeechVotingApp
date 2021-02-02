package com.northis.speechvotingapp.view.voting.recyclerview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.viewmodel.VotingViewModel

class VotingDetailsAdapter(
    private val context: Context?,
    private val lifecycleOwner: LifecycleOwner,
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
        val view = inflater.inflate(R.layout.voting_details_fragment_view_holder, parent, false)
        return DetailsFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailsFragmentViewHolder, position: Int) {
        val votingSpeech = vm.voting.VotingSpeeches[position]
        val speech = votingSpeech.Speech
        val creator = speech.Creator
        holder.speechTheme.text = speech.Theme
        holder.speechCreator.text = "${creator.FirstName} ${creator.LastName}"
        holder.holderLayout.setOnClickListener {
            Log.d("vote", "shortCLick")
            if (vm.voting.HasUserVoted) {
                vm.addVote(speech.SpeechId.toString()).observe(lifecycleOwner, {
                    Log.d("vote", it.code().toString())
                })
            } else {
                vm.switchVote(speech?.SpeechId.toString()).observe(lifecycleOwner, {
                    Log.d("vote", it.code().toString())
                })
            }
        }
        holder.holderLayout.setOnLongClickListener {
            Log.d("vote", "longCLick")
            if (votingSpeech.HasUserVoted) {
                vm.removeVote().observe(lifecycleOwner, {
                    Log.d("vote", it.code().toString())
                })
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return vm.voting.VotingSpeeches.size
    }

}