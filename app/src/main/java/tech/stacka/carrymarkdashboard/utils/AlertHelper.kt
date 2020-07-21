package tech.stacka.carrymarkdashboard.utils

import android.app.Activity
import android.graphics.Color
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import tech.stacka.carrymarkdashboard.R

object AlertHelper {
    fun showRetrySnackBarAlert(activity: Activity, text: String?, listener: SnackBarListener?): Snackbar {
        val snackbar = Snackbar.make(activity.findViewById(android.R.id.content), text!!, Snackbar.LENGTH_LONG)
            .setAction(activity.resources.getString(R.string.retry)) {
                try {
                    listener?.onOkClick()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        snackbar.setActionTextColor(Color.RED)
        val sbView = snackbar.view
        val textView = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbar.show()
        return snackbar
    }

    fun showNoInternetSnackBar(activity: Activity, listener: SnackBarListener?): Snackbar {
        return showRetrySnackBarAlert(activity, activity.getString(R.string.no_internet_connection), listener)
    }

    fun showOKSnackBarAlert(activity: Activity, text: String?): Snackbar {
        val snackbar = Snackbar.make(activity.findViewById(android.R.id.content), text!!, Snackbar.LENGTH_LONG)
            .setAction(activity.resources.getString(R.string.ok)) {
            }
        snackbar.setActionTextColor(Color.RED)
        val sbView = snackbar.view
        val textView =
            sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbar.show()
        return snackbar
    }

    fun showOKfinishSnackBarAlert(activity: Activity, text: String?): Snackbar {
        val snackbar = Snackbar.make(activity.findViewById(android.R.id.content), text!!, Snackbar.LENGTH_LONG)
            .setAction(activity.resources.getString(R.string.ok)) { activity.finish() }
        snackbar.setActionTextColor(Color.RED)
        val sbView = snackbar.view
        val textView = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        snackbar.show()
        return snackbar
    }

    interface SnackBarListener {
        fun onOkClick()
    }
}