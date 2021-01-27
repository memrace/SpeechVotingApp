package com.northis.speechvotingapp.view.voting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.authentication.AuthorizationService
import com.northis.speechvotingapp.authentication.OnTokenFailureListener
import com.northis.speechvotingapp.databinding.ActivityVotingBinding
import com.northis.speechvotingapp.di.App
import com.northis.speechvotingapp.di.component.ApiComponent
import com.northis.speechvotingapp.view.authorization.AuthActivity
import com.northis.speechvotingapp.view.ui.ActivityUIService
import com.northis.speechvotingapp.viewmodel.VotingViewModel
import com.northis.speechvotingapp.viewmodel.VotingViewModelFactory
import javax.inject.Inject


class VotingActivity : AppCompatActivity(), OnTokenFailureListener {

    @Inject
    internal lateinit var authorizationService: AuthorizationService

    // Инициализируем навигационный контроллер.
    internal lateinit var navController: NavController

    // ApiComponent
    internal lateinit var apiComponent: ApiComponent

    // ViewBinding
    internal lateinit var mBinding: ActivityVotingBinding
    private lateinit var activityUiService: ActivityUIService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // DI
        apiComponent = (applicationContext as App).apiComponent
        apiComponent.inject(this)
        (applicationContext as App)
            .apiComponent.inject(this)
        // Auth check
        authorizationService.checkAuthorization(this)
        // Binding
        mBinding = ActivityVotingBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        // Указываем ссылку на хост.
        navController = Navigation.findNavController(this, R.id.nav_voting)
        // UI
        activityUiService = ActivityUIService(
            this,
            mBinding.inclTopAppBar.topAppBar,
            mBinding.inclBottomNavigationBar.bottomNavigation,
            "Голосование",
            R.id.nav_voting
        )
        activityUiService.setActivityUi()
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

    override fun onTokenFailure() {
        startActivityForResult(Intent(this, AuthActivity::class.java), 1)
        Log.d("TOKENS", "NO TOKENS")
    }

}