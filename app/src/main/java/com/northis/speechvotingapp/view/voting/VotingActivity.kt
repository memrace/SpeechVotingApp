package com.northis.speechvotingapp.view.voting

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.databinding.ActivityVotingBinding
import com.northis.speechvotingapp.di.App
import com.northis.speechvotingapp.view.authorization.AuthActivity
import com.northis.speechvotingapp.view.ui.ActivityUIService
import com.northis.speechvotingapp.viewmodel.VotingViewModel


class VotingActivity : AppCompatActivity() {
    private val votingViewModel: VotingViewModel by viewModels()

    // Инициализируем навигационный контроллер.
    private lateinit var navController: NavController

    // ViewBinding
    private lateinit var mBinding: ActivityVotingBinding
    private lateinit var activityUiService: ActivityUIService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityVotingBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        (applicationContext as App)
            .apiComponent.inject(this)
        // Указываем ссылку на хост.
        navController = Navigation.findNavController(this, R.id.nav_voting)
        activityUiService = ActivityUIService(
            this,
            mBinding.inclTopAppBar.topAppBar,
            mBinding.inclBottomNavigationBar.bottomNavigation,
            "Голосование",
            R.id.nav_voting
        )
        activityUiService.setActivityUi()
        mBinding.button.setOnClickListener {
            startActivityForResult(Intent(this, AuthActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == -1) {
                with(Toast.makeText(this, "Добро пожаловать!", Toast.LENGTH_SHORT)) {
                    show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onStart() {
        super.onStart()
        votingViewModel.votingListLiveData.observe(this, Observer { })
        votingViewModel.votingLiveData.observe(this, Observer { })
        votingViewModel.winnerLiveData.observe(this, Observer { })
    }

    private fun getVoting(votingId: String) {
        votingViewModel.loadVoting(votingId)
    }

    private fun getWinner(votingId: String) {
        votingViewModel.loadWinner(votingId)
    }

}