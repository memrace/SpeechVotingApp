package com.northis.speechvotingapp.view.voting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.databinding.FragmentVotingAddScheduleBinding
import com.northis.speechvotingapp.databinding.FragmentVotingDetailsBinding
import com.northis.speechvotingapp.viewmodel.VotingViewModel
import com.northis.speechvotingapp.viewmodel.VotingViewModelFactory
import javax.inject.Inject


class VotingAddScheduleFragment : Fragment() {
    private var _binding: FragmentVotingAddScheduleBinding? = null
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
        _binding = FragmentVotingAddScheduleBinding.inflate(inflater, container, false)
        votingActivity = (activity as VotingActivity)
        votingActivity.apiComponent.inject(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        votingViewModel.getWinner().observe(viewLifecycleOwner, {
            if (it != null) {
                votingViewModel.speechWinnerId = it.SpeechId.toString()
            }
        })
        votingViewModel.getProfiles().observe(viewLifecycleOwner, {

        })
        votingViewModel.speechDate.observe(viewLifecycleOwner, {
            binding.fabSetExecutor.visibility = View.VISIBLE
            votingViewModel.setExecutor(it)
        })

    }
}