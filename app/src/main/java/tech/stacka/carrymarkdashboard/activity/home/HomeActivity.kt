package tech.stacka.carrymarkdashboard.activity.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.addAdmin.AddAdminActivity
import tech.stacka.carrymarkdashboard.activity.distributer.addDistributer.AddDistributerActivity
import tech.stacka.carrymarkdashboard.activity.distributer.distributerList.DistributorListActivity
import tech.stacka.carrymarkdashboard.activity.employee.addEmployee.AddEmployeeActivity
import tech.stacka.carrymarkdashboard.activity.employee.employeeList.EmployeeListActivity
import tech.stacka.carrymarkdashboard.activity.login.LoginActivity
import tech.stacka.carrymarkdashboard.activity.master.addMaster.AddMasterActivity
import tech.stacka.carrymarkdashboard.activity.master.masterList.MasterListActivity
import tech.stacka.carrymarkdashboard.activity.notification.addNotification.AddNotificationActivity
import tech.stacka.carrymarkdashboard.activity.notification.listNotification.NotificationListActivity
import tech.stacka.carrymarkdashboard.activity.order.orderList.OrderListActivity
import tech.stacka.carrymarkdashboard.activity.product.addProduct.AddProductActivity
import tech.stacka.carrymarkdashboard.activity.product.productList.ProductListActivity
import tech.stacka.carrymarkdashboard.activity.retailer.retailerList.RetailerListActivity
import tech.stacka.carrymarkdashboard.models.ReportResponse
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    HomeView {
    var strToken:String=""
    val presenter=HomePresenter(this@HomeActivity,this@HomeActivity);
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        strToken=SharedPrefManager.getInstance(this).user.strToken!!
        presenter.getReport(strToken)
        setSupportActionBar(main_toolbar)


        nav_toggle.setOnClickListener {
            drawer_layout.openDrawer(Gravity.LEFT)
        }
        navProfile()
        bt_sign_out.setOnClickListener {
            showDialog()
        }
        nav_view.setNavigationItemSelectedListener(this)

        notifButton.setOnClickListener {
            startActivity(Intent(this@HomeActivity,NotificationListActivity::class.java))
        }



    }

    override fun onResume() {
        super.onResume()
        nav_view.setCheckedItem(R.id.nav_dashboard)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dashboard -> {
             //   Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_productList -> {
                startActivity(Intent(this@HomeActivity, ProductListActivity::class.java))
            }
            R.id.nav_addProduct -> {
                startActivity(Intent(this@HomeActivity, AddProductActivity::class.java))
            }
            R.id.nav_employeeList -> {
                startActivity(Intent(this@HomeActivity, EmployeeListActivity::class.java))
            }
            R.id.nav_distributorList -> {
                startActivity(Intent(this@HomeActivity, DistributorListActivity::class.java))
            }
            R.id.nav_retailerList -> {
                startActivity(Intent(this@HomeActivity, RetailerListActivity::class.java))
            }
            R.id.nav_addDistributor -> {
                startActivity(Intent(this@HomeActivity, AddDistributerActivity::class.java))
            }
            R.id.nav_addEmployee -> {
                startActivity(Intent(this@HomeActivity, AddEmployeeActivity::class.java))
            }
            R.id.nav_addMaster -> {
                startActivity(Intent(this@HomeActivity, AddMasterActivity::class.java))
            }
            R.id.nav_master -> {
                startActivity(Intent(this@HomeActivity, MasterListActivity::class.java))
            }

            R.id.nav_orderList -> {
                startActivity(Intent(this@HomeActivity, OrderListActivity::class.java))
            }
            R.id.nav_notific -> {
                startActivity(Intent(this@HomeActivity, NotificationListActivity::class.java))
            }
            R.id.nav_send_notific -> {
                startActivity(Intent(this@HomeActivity, AddNotificationActivity::class.java))
            }
            R.id.nav_addAdmin -> {
                startActivity(Intent(this@HomeActivity, AddAdminActivity::class.java))
            }

        }
        drawer_layout.closeDrawer(Gravity.LEFT)
        return false
    }



    private fun createOrderChart(deliveredOrders:Int,pendingOrders:Int,confirmedOrder:Int,rejectedOrders:Int) {

        //<_--Populate Chart-->
        chartOrders.setUsePercentValues(false)
        chartOrders.description.isEnabled = false
        chartOrders.setExtraOffsets(5F, 10F, 5F, 5F)
        chartOrders.dragDecelerationFrictionCoef = 0.9f
        chartOrders.transparentCircleRadius = 56f
        chartOrders.setHoleColor(Color.WHITE)
        chartOrders.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        chartOrders.legend.isEnabled = true
        chartOrders.setDrawEntryLabels(false)
        chartOrders.animateY(1500, Easing.EaseInOutCubic)

        //Adding Center Text
        val str =
            (deliveredOrders + pendingOrders + confirmedOrder + rejectedOrders).toString() + "\nTotal Orders"
        val centeredText = SpannableString(str)
        centeredText.setSpan(RelativeSizeSpan(1.8f), 0, str.indexOf("\n"), 0)
        chartOrders.centerText = centeredText

        //Creating entries for order chart
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(deliveredOrders.toFloat(), "Delivered"))
        entries.add(PieEntry(pendingOrders.toFloat(), "Pending"))
        entries.add(PieEntry(confirmedOrder.toFloat(), "Confirmed"))
        entries.add(PieEntry(rejectedOrders.toFloat(), "Rejected"))

        //Adding entries to a dataset
        val dataSet = PieDataSet(entries, null)
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        val COLORFUL_COLORS = intArrayOf(
            Color.rgb(106, 150, 31),
            Color.rgb(193, 37, 82),
            Color.rgb(245, 199, 0),
            Color.rgb(255, 52, 0)
        )
        dataSet.setColors(*COLORFUL_COLORS)

        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(12f)
        pieData.setValueTextColor(Color.WHITE)
        chartOrders.data = pieData

    }


    private fun createTargetChart(progress: Float) {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(progress, "Completed"))
        val prg = if ((100 - progress) > 0) {
            100 - progress
        } else {
            0F
        }
        entries.add(PieEntry((prg), "Rest"))
        val dataSet = PieDataSet(entries, "Emp")
        val COLORFUL_COLORS = listOf(
            Color.rgb(23, 150, 254),
            Color.rgb(242, 242, 242)
        )
        dataSet.colors = COLORFUL_COLORS
        val pieData = PieData(dataSet)
        pieData.setDrawValues(false)

        val str = "${progress.toInt()}%\nCompleted"
        val centeredText = SpannableString(str)
        centeredText.setSpan(RelativeSizeSpan(1.8f), 0, str.indexOf("\n"), 0)

        chartEmpTarget.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        chartEmpTarget.setCenterTextSize(18f)
        chartEmpTarget.centerText = centeredText
        chartEmpTarget.setUsePercentValues(false)
        chartEmpTarget.setExtraOffsets(5f, 10f, 5f, 5f)
        chartEmpTarget.description.isEnabled = false
        chartEmpTarget.legend.isEnabled = false
        chartEmpTarget.setHoleColor(Color.WHITE)
        chartEmpTarget.holeRadius = 60f
        chartEmpTarget.transparentCircleRadius = 56f
        chartEmpTarget.setDrawEntryLabels(false)
        chartEmpTarget.animateY(1500, Easing.EaseInOutCubic)
        chartEmpTarget.data = pieData
        chartEmpTarget.setTouchEnabled(false)
    }

    private fun showDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.normal_pop_up)
        val heading =
            dialog.findViewById<View>(R.id.popupTextHead) as TextView
        heading.text="Are you sure you want to logout?"
        val details =
            dialog.findViewById<View>(R.id.popupTextDetail) as TextView
        val btYes =
            dialog.findViewById<View>(R.id.btYesPopup) as Button
        val btNo =
            dialog.findViewById<View>(R.id.btNoPopUp) as Button
        btYes.setOnClickListener {
            SharedPrefManager.getInstance(this).clear()
            val i = Intent(this@HomeActivity, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            dialog.dismiss()
        }
        btNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showExitDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.normal_pop_up)
        val heading =
            dialog.findViewById<View>(R.id.popupTextHead) as TextView
        heading.text="Are you sure you want to exit?"
        val details =
            dialog.findViewById<View>(R.id.popupTextDetail) as TextView
        val btYes =
            dialog.findViewById<View>(R.id.btYesPopup) as Button
        val btNo =
            dialog.findViewById<View>(R.id.btNoPopUp) as Button
        btYes.setOnClickListener {
            ActivityCompat.finishAffinity(this@HomeActivity)
            dialog.dismiss()
        }
        btNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun navProfile(){
        val mNavigation = findViewById<NavigationView>(R.id.nav_view)
        val mHeaderView = mNavigation.getHeaderView(0)
        val mDrawerHeaderTitle = mHeaderView.findViewById(R.id.nav_prof_name) as TextView
        mDrawerHeaderTitle.text=SharedPrefManager.getInstance(this).user.strName.toString()

    }

    override fun onBackPressed() {
        showExitDialog()
    }

    override fun onReportSuccess(apiResponse: ReportResponse) {
        val target: Long = apiResponse.SALE.dblSaleTarget.toLong()
        val completedTarget: Long = apiResponse.SALE.dblTotalOrderAmount.toLong()

        if (target != 0L) {
            val targetPercentage = (completedTarget * 100) / target
            createTargetChart(targetPercentage.toFloat())
        }

        createOrderChart(apiResponse.ORDER.DELIVERED,apiResponse.ORDER.PENDING,apiResponse.ORDER.CONFIRM,apiResponse.ORDER.CANCEL)


    }

    override fun onReportNull(apiResponse: ReportResponse) {
        Toast.makeText(this@HomeActivity,apiResponse.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun onReportFailed(apiResponse: JSONArray) {
        val listData = ArrayList<String>()
        for (i in 0 until apiResponse.length()) {
            listData += apiResponse.getString(i)
            Log.e("ListData",listData.toString())
            for(i in listData){
                when (i) {
                    "AUTHORIZATION_TOKEN_HEADER_IS_MISSING" -> {
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    "INVALID_TOKEN_PROVIDED" -> {
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    else -> {

                        Toast.makeText(this@HomeActivity,i.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onReportServerError(string: String) {
        Toast.makeText(this@HomeActivity,string,Toast.LENGTH_SHORT).show()

    }
}