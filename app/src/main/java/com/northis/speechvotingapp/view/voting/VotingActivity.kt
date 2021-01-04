package com.northis.speechvotingapp.view.voting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.northis.speechvotingapp.R

class VotingActivity : AppCompatActivity() {
    // Инициализируем навигационный контроллер.
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)
        // Указываем ему ссылку на хост.
        navController = Navigation.findNavController(this, R.id.nav_voting)
    }
}