package tech.stacka.carrymarkdashboard.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import tech.stacka.carrymarkdashboard.BuildConfig
import java.util.regex.Matcher
import java.util.regex.Pattern

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

        fun isValidPhoneNumber(target: CharSequence): Boolean {
            return if (target.length == 10) {
                Patterns.PHONE.matcher(target).matches()
            } else {
                false
            }
        }

        fun emailValidator(email: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            pattern = Pattern.compile(EMAIL_PATTERN)
            matcher = pattern.matcher(email)
            return matcher.matches()
        }
    }

}