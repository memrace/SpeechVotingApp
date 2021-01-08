package com.northis.speechvotingapp.view.voting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.databinding.ActivityVotingBinding
import com.northis.speechvotingapp.view.authorization.AuthActivity
import com.northis.speechvotingapp.view.ui.ActivityUIService


class VotingActivity : AppCompatActivity() {
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
        // Указываем ссылку на хост.
        navController = Navigation.findNavController(this, R.id.nav_voting)
        activityUiService = ActivityUIService(this, mBinding.inclTopAppBar.topAppBar, mBinding.inclBottomNavigationBar.bottomNavigation, "Голосование", R.id.nav_voting)
        activityUiService.setActivityUi()
        mBinding.button.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }
    }

}