package com.northis.speechvotingapp.view.ui

import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.view.catalog.CatalogActivity
import com.northis.speechvotingapp.view.schedule.ScheduleActivity
import com.northis.speechvotingapp.view.voting.VotingActivity

class ActivityUIService(
    private val activity: AppCompatActivity,
    private val toolbar: Toolbar?,
    private val navigation: BottomNavigationView,
    private val title: String,
    private val itemId: Int
) {


    fun setActivityUi() {
        setAppBar()
        setNavigation()
    }

    private fun setAppBar() {
        activity.setSupportActionBar(toolbar)
        toolbar?.title = title
    }

    private fun setNavigation() {
        navigation.selectedItemId = itemId
        navigation.setOnNavigationItemSelectedListener(createNavigationItemSelectedListener())
    }

    private fun createNavigationItemSelectedListener(): BottomNavigationView.OnNavigationItemSelectedListener? {
        return BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_voting -> {
                    activity.startActivity(
                        Intent(
                            activity.applicationContext,
                            VotingActivity::class.java
                        )
                    )
                    true
                }
                R.id.nav_schedule -> {
                    activity.startActivity(
                        Intent(
                            activity.applicationContext,
                            ScheduleActivity::class.java
                        )
                    )
                    true
                }
                R.id.nav_catalog -> {
                    activity.startActivity(
                        Intent(
                            activity.applicationContext,
                            CatalogActivity::class.java
                        )
                    )
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}