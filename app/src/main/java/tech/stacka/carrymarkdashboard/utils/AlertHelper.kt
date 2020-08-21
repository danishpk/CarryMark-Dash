package tech.stacka.carrymarkdashboard.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.login.LoginActivity
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager

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

    fun showDialog(context: Context,heading:String,details:String) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.normal_pop_up_custom)
        val heading = dialog.findViewById<View>(R.id.tvCustomPopupHeading) as TextView
        heading.text = heading.toString()
        val details = dialog.findViewById<View>(R.id.tvCustomPopupDetail) as TextView
        details.text=details.toString()
        val btYes = dialog.findViewById<View>(R.id.btCustomPopupOk) as Button
        val btNo = dialog.findViewById<View>(R.id.btCustomPopupCancel) as Button
        btYes.setOnClickListener {

            dialog.dismiss()
        }
        btNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    interface SnackBarListener {
        fun onOkClick()
    }
}