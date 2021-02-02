package com.northis.speechvotingapp.view.voting.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.model.Speeches
import com.northis.speechvotingapp.viewmodel.VotingViewModel

class VotingAddSpeechAdapter(
    private val context: Context?,
    private val speeches: Speeches,
    private val vm: VotingViewModel
) : RecyclerView.Adapter<VotingAddSpeechAdapter.AddSpeechViewHolder>() {
    class AddSpeechViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val speechTheme: TextView by lazy { itemView.findViewById(R.id.voting_add_speech_theme) }
        internal val speechDescription: TextView by lazy { itemView.findViewById(R.id.voting_add_speech_descr) }
        internal val creatorImage: ImageView by lazy { itemView.findViewById(R.id.voting_add_speech_img) }
        internal val cardView: CardView by lazy { itemView.findViewById(R.id.voting_add_speech_card) }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddSpeechViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.voting_add_speech_to_voting_view_holder, parent, false)
        return AddSpeechViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddSpeechViewHolder, position: Int) {
        val speeches = speeches.Speeches
        holder.speechTheme.text = speeches[position].Theme
        holder.speechDescription.text = speeches[position].Description
        // TODO IMG
        holder.cardView.setOnClickListener {
            vm.speechId.value = speeches[position].SpeechId.toString()
        }
    }

    override fun getItemCount(): Int {
        return speeches.Speeches.size
    }
}