package com.northis.speechvotingapp.view.authorization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.northis.speechvotingapp.authentication.AuthorizationService
import com.northis.speechvotingapp.databinding.ActivityAuthBinding
import com.northis.speechvotingapp.di.App
import javax.inject.Inject


class AuthActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityAuthBinding
    @Inject
    internal lateinit var authServe: AuthorizationService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAuthBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        (applicationContext as App).oauthComponent.inject(this)
        authServe.beginAuthentication(mBinding.authView)
    }

}