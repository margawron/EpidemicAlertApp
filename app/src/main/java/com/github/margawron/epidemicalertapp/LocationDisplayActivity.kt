package com.github.margawron.epidemicalertapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.databinding.LocationDisplayActivityBinding
import com.github.margawron.epidemicalertapp.fragments.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationDisplayActivity: AppCompatActivity() {

    companion object{
        const val FRAGMENT_ID_KEY = "FRAGMENT_ID"
    }

    @Inject
    lateinit var authManager: AuthManager
    private val poiLocationFragment = PoiLocationFragment()
    private val locationHistoryFragment = LocationHistoryFragment()
    private val zoneFragment = ZoneFragment()
    private val alertFragment = AlertFragment()
    private val statisticsFragment = StatisticsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(authManager.getToken() == null){
            val loginIntent = Intent(this, LoginActivity::class.java)
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(loginIntent)
        } else {

            val binding: LocationDisplayActivityBinding =
                DataBindingUtil.setContentView(this, R.layout.location_display_activity)


            val fragmentId = savedInstanceState?.getInt(FRAGMENT_ID_KEY) ?: R.id.ic_poi
            selectFragmentById(fragmentId)

            binding.bottomNavigation.setOnNavigationItemSelectedListener {
                savedInstanceState?.putInt(FRAGMENT_ID_KEY, it.itemId)
                selectFragmentById(it.itemId)
                true
            }
        }
    }

    private fun selectFragmentById(itemId: Int) {
        when (itemId) {
            R.id.ic_poi -> changeCurrentlyDisplayedFragment(poiLocationFragment)
            R.id.ic_location_history -> changeCurrentlyDisplayedFragment(
                locationHistoryFragment
            )
            R.id.ic_zones -> changeCurrentlyDisplayedFragment(zoneFragment)
            R.id.ic_alerts -> changeCurrentlyDisplayedFragment(alertFragment)
            R.id.ic_statistics -> changeCurrentlyDisplayedFragment(statisticsFragment)
        }
    }

    private fun changeCurrentlyDisplayedFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_wrapper, fragment)
            commit()
        }
    }
}