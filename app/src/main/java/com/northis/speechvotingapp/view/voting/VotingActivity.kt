package com.northis.speechvotingapp.view.voting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.databinding.ActivityVotingBinding
import com.northis.speechvotingapp.di.App
import com.northis.speechvotingapp.model.User
import com.northis.speechvotingapp.network.IUserService
import com.northis.speechvotingapp.view.authorization.AuthActivity
import com.northis.speechvotingapp.view.ui.ActivityUIService
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class VotingActivity : AppCompatActivity() {
    // Инициализируем навигационный контроллер.
    private lateinit var navController: NavController

    @Inject
    internal lateinit var userService: IUserService
    @Inject
    internal lateinit var client: HttpClient
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
        mBinding.button2.setOnClickListener {

                test()

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

     fun test(){
        val call = userService.getUsers()
        call.enqueue(object :Callback<Array<User>>{
            override fun onFailure(call: Call<Array<User>>, t: Throwable) {
                t.message?.let { Log.d("problem", it) }
            }

            override fun onResponse(
                call: Call<Array<User>>,
                response: Response<Array<User>>
            ) {
                val users = response.body() as Array<User>
                Log.d("Got it!", users[0].Email)

            }
        })
    }

}