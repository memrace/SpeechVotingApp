package com.northis.speechvotingapp.view.catalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.northis.speechvotingapp.R

class CatalogActivity : AppCompatActivity() {
    lateinit var navController: NavController;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)
        navController = Navigation.findNavController(this, R.id.nav_catalog)
    }
}