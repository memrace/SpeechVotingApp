package com.northis.speechvotingapp.view.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.northis.speechvotingapp.R
import com.northis.speechvotingapp.databinding.ActivityScheduleBinding
import com.northis.speechvotingapp.view.ui.ActivityUIService

class ScheduleActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityScheduleBinding
    private lateinit var activityUiService: ActivityUIService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityScheduleBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        activityUiService = ActivityUIService(
            this,
            mBinding.inclTopAppBar.topAppBar,
            mBinding.inclBottomNavigationBar.bottomNavigation,
            "Расписание",
            R.id.nav_schedule
        )
        activityUiService.setActivityUi()
    }
}