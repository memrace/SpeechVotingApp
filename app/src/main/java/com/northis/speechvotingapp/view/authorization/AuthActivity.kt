package com.northis.speechvotingapp.view.authorization


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.northis.speechvotingapp.authentication.AuthorizationService
import com.northis.speechvotingapp.authentication.OnTokenAcquiredListener
import com.northis.speechvotingapp.databinding.ActivityAuthBinding
import com.northis.speechvotingapp.di.App
import com.northis.speechvotingapp.view.voting.VotingActivity
import javax.inject.Inject


class AuthActivity : AppCompatActivity(), OnTokenAcquiredListener {
    private lateinit var mBinding: ActivityAuthBinding
    @Inject
    internal lateinit var authServe: AuthorizationService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAuthBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        (applicationContext as App).oauthComponent.inject(this)
        mBinding.loginBtn.setOnClickListener {
            mBinding.authView.visibility = View.VISIBLE
            authServe.startAuthentication(mBinding.authView, this)
        }
    }

    override fun onTokenAcquired() {
        setResult(-1, Intent(this, VotingActivity::class.java))
        finish()
    }
}