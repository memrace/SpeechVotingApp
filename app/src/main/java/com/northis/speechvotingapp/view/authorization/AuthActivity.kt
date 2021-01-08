package com.northis.speechvotingapp.view.authorization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.northis.speechvotingapp.authentication.AuthorizationService
import com.northis.speechvotingapp.authentication.IUserTokenStorage
import com.northis.speechvotingapp.databinding.ActivityAuthBinding
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*



class AuthActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityAuthBinding
    private lateinit var authServe: AuthorizationService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAuthBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)

        authServe = AuthorizationService(this, IUserTokenStorage.instance, mBinding.authView, HttpClient(Android) {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    serializeNulls()
                    disableHtmlEscaping()
                }
            }
        })

        authServe.beginAuthentication()
    }

}