package com.github.margawron.epidemicalertapp.util

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionSafeguard(
    private val sourceActivity: Activity,
    private val requestedPermissions: Array<String>,
    private val requestCode: Int
) {


    fun doIfPossibleOrRequestPermission(wrappedFunction: () -> Unit) {
        var isGranted = PackageManager.PERMISSION_GRANTED
        for (permission in requestedPermissions) {
            if (ContextCompat.checkSelfPermission(
                    sourceActivity.applicationContext,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                isGranted = PackageManager.PERMISSION_DENIED
                break
            }
        }

        when {
            isGranted == PackageManager.PERMISSION_GRANTED -> {
                wrappedFunction()
            }
            else -> {
                ActivityCompat.requestPermissions(sourceActivity, requestedPermissions, requestCode)
            }
        }
    }
}