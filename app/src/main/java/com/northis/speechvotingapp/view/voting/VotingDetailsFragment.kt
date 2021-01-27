package com.northis.speechvotingapp.view.voting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.databinding.FragmentVotingDetailsBinding
import com.northis.speechvotingapp.view.voting.recyclerview.VotingDetailsAdapter
import com.northis.speechvotingapp.viewmodel.VotingViewModel
import com.northis.speechvotingapp.viewmodel.VotingViewModelFactory
import javax.inject.Inject

class VotingDetailsFragment : Fragment() {
    private var _binding: FragmentVotingDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    internal lateinit var votingViewModelFactory: VotingViewModelFactory
    private val votingViewModel: VotingViewModel by activityViewModels(factoryProducer = { votingViewModelFactory })
    private lateinit var votingActivity: VotingActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVotingDetailsBinding.inflate(inflater, container, false)
        votingActivity = (activity as VotingActivity)
        votingActivity.apiComponent.inject(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val votingRecyclerView = binding.votingRecyclerView
        val appBar = votingActivity.mBinding.inclTopAppBar.topAppBar
        votingViewModel.getVoting().observe(viewLifecycleOwner, {
            Log.d("VOTING", it.toString())
            if (it != null) {
                votingViewModel.voting = it
                votingRecyclerView.adapter =
                    VotingDetailsAdapter(context, viewLifecycleOwner, votingViewModel)
                votingRecyclerView.layoutManager = LinearLayoutManager(context)
                appBar.title = it.Title
                binding.votingEndDate.text = it.EndDate?.toString() ?: "Голосование ещё не начато"
            }

        })
        if (!votingViewModel.voting.HasEnded){
            binding.floatingActionButton.setImageResource(R.drawable.ic_add_circle_24px)
            binding.floatingActionButton.setOnClickListener {
                votingActivity.navController.navigate(R.id.action_votingDetailsFragment_to_votingAddSpeechFragment)
            }
        } else {
            binding.floatingActionButton.setImageResource(R.drawable.ic_calendar_today_24px)
            binding.floatingActionButton.setOnClickListener {
                votingActivity.navController.navigate(R.id.action_votingDetailsFragment_to_votingAddScheduleFragment)
            }
        }

    }

}