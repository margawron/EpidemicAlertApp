package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.margawron.epidemicalertapp.databinding.LocationDisplayActivityBinding
import com.github.margawron.epidemicalertapp.fragments.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDisplayActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: LocationDisplayActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.location_display_activity)

        val poiLocationFragment = PoiLocationFragment()
        val locationHistoryFragment = LocationHistoryFragment()
        val zoneFragment = ZoneFragment()
        val alertFragment = AlertFragment()
        val statisticsFragment = StatisticsFragment()

        changeCurrentlyDisplayedFragment(poiLocationFragment)

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_poi -> changeCurrentlyDisplayedFragment(poiLocationFragment)
                R.id.ic_location_history -> changeCurrentlyDisplayedFragment(locationHistoryFragment)
                R.id.ic_zones -> changeCurrentlyDisplayedFragment(zoneFragment)
                R.id.ic_alerts -> changeCurrentlyDisplayedFragment(alertFragment)
                R.id.ic_statistics -> changeCurrentlyDisplayedFragment(statisticsFragment)
            }
            true
        }
    }

    private fun changeCurrentlyDisplayedFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_wrapper, fragment)
            commit()
        }
    }
}