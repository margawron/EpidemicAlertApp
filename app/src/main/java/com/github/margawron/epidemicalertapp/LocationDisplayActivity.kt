package com.github.margawron.epidemicalertapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.margawron.epidemicalertapp.api.alerts.AlertService
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.data.users.Role
import com.github.margawron.epidemicalertapp.databinding.LocationDisplayActivityBinding
import com.github.margawron.epidemicalertapp.dialogs.AlertDetailsDialog
import com.github.margawron.epidemicalertapp.fragments.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LocationDisplayActivity : AppCompatActivity() {

    companion object {
        const val FRAGMENT_ID_KEY = "FRAGMENT_ID"
    }

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var alertService: AlertService

    private val poiLocationFragment = PoiLocationFragment()
    private val locationHistoryFragment = LocationHistoryFragment()
    private val zoneFragment = ZoneFragment()
    private val alertFragment = AlertFragment()
    private val statisticsFragment = StatisticsFragment()

    lateinit var toolbarMenu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!authManager.isUserLoggedIn()) {
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
            configureToolbarMenu(binding.locationDisplayToolbar.menu)
            binding.locationDisplayToolbar.setOnMenuItemClickListener(menuListeners())
            Log.e(this::class.simpleName,
                if(intent.extras == null) "extras are empty" else "extras not empty"
            )
            intent.extras?.let{ extras ->
                val alertId = extras.get("alertId") as Long?
                Log.e(this::class.simpleName, "AlertId ${alertId ?: "null"}")
                alertId?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        val response = withContext(Dispatchers.IO){
                            return@withContext alertService.getSingleAlert(alertId)
                        }
                        when(response){
                            is ApiResponse.Success -> {
                                AlertDetailsDialog(response.body!!)
                                    .show(supportFragmentManager, AlertDetailsDialog.DIALOG_TAG)
                            }
                            is ApiResponse.Error -> {
                                val errors = ApiResponse.errorToMessage(response)
                                Log.w("OE", errors.toString())
                                Toast.makeText(this@LocationDisplayActivity, errors, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun menuListeners(): Toolbar.OnMenuItemClickListener =
        Toolbar.OnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_bar_settings -> {
                    val settingsIntent = Intent(this, SettingsActivity::class.java)
                    startActivity(settingsIntent)
                }
                R.id.action_bar_change_privilages -> {
                    val changePrivilegesIntent = Intent(this, ChangePrivilegesActivity::class.java)
                    startActivity(changePrivilegesIntent)
                }
                R.id.action_bar_mark_place -> {
                    val markPlaceIntent = Intent(this, MarkPlaceActivity::class.java)
                    startActivity(markPlaceIntent)
                }
                R.id.action_bar_mark_user -> {
                    val markUserIntent = Intent(this, MarkUserActivity::class.java)
                    startActivity(markUserIntent)
                }
                else -> {
                }
            }
            false
        }


    private fun configureToolbarMenu(toolbarMenu: Menu) {
        val markUserItem = toolbarMenu.findItem(R.id.action_bar_mark_user)
        val markPlaceItem = toolbarMenu.findItem(R.id.action_bar_mark_place)
        val changePrivilegesItem = toolbarMenu.findItem(R.id.action_bar_change_privilages)
        when (authManager.getLoggedInUser().role) {
            Role.USER -> {
                markUserItem.isVisible = false
                markPlaceItem.isVisible = false
                changePrivilegesItem.isVisible = false
            }
            Role.MODERATOR -> {
                changePrivilegesItem.isVisible = false
            }
            Role.ADMINISTRATOR -> {
                markUserItem.isVisible = false
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