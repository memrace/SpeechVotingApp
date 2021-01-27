package com.northis.speechvotingapp.view.voting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.northis.speechvotingapp.databinding.FragmentVotingAddSpeechBinding
import com.northis.speechvotingapp.view.voting.recyclerview.VotingAddSpeechAdapter
import com.northis.speechvotingapp.viewmodel.VotingViewModel
import com.northis.speechvotingapp.viewmodel.VotingViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VotingAddSpeechFragment : Fragment() {
    private var _binding: FragmentVotingAddSpeechBinding? = null
    private val binding get() = _binding!!

    @Inject
    internal lateinit var votingViewModelFactory: VotingViewModelFactory
    private val votingViewModel: VotingViewModel by activityViewModels(factoryProducer = { votingViewModelFactory })
    private lateinit var votingActivity: VotingActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVotingAddSpeechBinding.inflate(inflater, container, false)
        votingActivity = (activity as VotingActivity)
        votingActivity.apiComponent.inject(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val speechesRV = binding.votingAddSpeechRV
        votingViewModel.loadSpeeches().observe(viewLifecycleOwner, {
            Log.d("Speeches", it.toString())
            if (it != null) {
                speechesRV.adapter = VotingAddSpeechAdapter(context, it, votingViewModel)
                speechesRV.layoutManager = LinearLayoutManager(context)
            }
        })
        votingViewModel.speechId.observe(viewLifecycleOwner, {
            lifecycleScope.launch(Dispatchers.Main) {
                val response = withContext(Dispatchers.IO) {
                    async {
                        votingViewModel.addSpeechToVoting(it)
                    }
                }
                response.await().observe(viewLifecycleOwner, {
                    with(Toast.makeText(context, it.code(), Toast.LENGTH_SHORT)) {
                        show()
                    }
                })

            }
        })
    }

}