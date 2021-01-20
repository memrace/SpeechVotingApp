package com.northis.speechvotingapp.view.voting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.northis.speechvotingapp.databinding.FragmentVotingDetailsBinding
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
    private lateinit var votingId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVotingDetailsBinding.inflate(inflater, container, false)
        votingActivity = (activity as VotingActivity)
        votingActivity.apiComponent.inject(this)
        votingId = arguments?.getString("votingId").toString()
        Log.d("VOTINGID", votingId)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        votingViewModel.getVoting(votingId).observe(viewLifecycleOwner, {
            Log.d("VOTING", it.toString())
        })
    }

}