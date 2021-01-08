package com.northis.speechvotingapp.view.catalog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.databinding.ActivityCatalogBinding
import com.northis.speechvotingapp.view.ui.ActivityUIService

class CatalogActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityCatalogBinding
    lateinit var navController: NavController
    private lateinit var activityUiService: ActivityUIService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCatalogBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        navController = Navigation.findNavController(this, R.id.nav_catalog)
        activityUiService = ActivityUIService(
            this,
            mBinding.inclTopAppBar.topAppBar,
            mBinding.inclBottomNavigationBar.bottomNavigation,
            "Каталог",
            R.id.nav_catalog
        )
        activityUiService.setActivityUi()
    }
}