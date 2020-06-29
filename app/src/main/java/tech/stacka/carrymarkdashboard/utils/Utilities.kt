package tech.stacka.carrymarkdashboard.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import tech.stacka.carrymarkdashboard.BuildConfig

class Utilities(private val context: Context) {


    companion object {

        fun checkInternetConnection(context: Context): Boolean {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (cm.activeNetworkInfo != null && cm.activeNetworkInfo
                    .isAvailable && cm.activeNetworkInfo.isConnected
            ) {
                true
            } else {
                false
            }
        }

        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager =
                activity.getSystemService(
                    Activity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
            if (BuildConfig.DEBUG && !(activity.currentFocus != null)) {
                error("Assertion failed")
            }
            if (activity.currentFocus != null) inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }

}