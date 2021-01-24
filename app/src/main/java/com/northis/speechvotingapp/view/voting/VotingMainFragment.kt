package com.northis.speechvotingapp.view.voting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.northis.speechvotingapp.databinding.FragmentVotingMainBinding
import com.northis.speechvotingapp.view.voting.recyclerview.VotingMainAdapter
import com.northis.speechvotingapp.viewmodel.VotingViewModel
import com.northis.speechvotingapp.viewmodel.VotingViewModelFactory
import javax.inject.Inject

class VotingMainFragment : Fragment() {
    private var _binding: FragmentVotingMainBinding? = null
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
        _binding = FragmentVotingMainBinding.inflate(inflater, container, false)
        votingActivity = (activity as VotingActivity)
        votingActivity.apiComponent.inject(this)
        votingActivity.mBinding.inclTopAppBar.topAppBar.title = "Голосование"
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val votingListRecyclerView = binding.votingListRecyclerView
        votingViewModel.getVotingList().observe(viewLifecycleOwner, {
            if (it != null) {
                votingListRecyclerView.adapter =
                    VotingMainAdapter(
                        context,
                        it,
                        votingActivity.navController,
                        votingViewModel
                    )
                votingListRecyclerView.layoutManager = LinearLayoutManager(context)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}