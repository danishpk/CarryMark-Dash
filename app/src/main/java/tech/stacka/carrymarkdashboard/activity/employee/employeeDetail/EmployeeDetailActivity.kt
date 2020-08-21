package tech.stacka.carrymarkdashboard.activity.employee.employeeDetail

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.activity_employee_detail.*

import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.employee.employeeList.EmployeeListActivity
import tech.stacka.carrymarkdashboard.activity.employee.targetHistory.TargetHistoryActivity
import tech.stacka.carrymarkdashboard.activity.employee.updateEmployee.UpdateEmployeeActivity
import tech.stacka.carrymarkdashboard.activity.map.MapActivity
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.EmployeeDetailResponse
import tech.stacka.carrymarkdashboard.models.ReportResponse
import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities
import kotlin.collections.ArrayList

class EmployeeDetailActivity : AppCompatActivity(), EmployeeDetailView {

    var strEmployeeId:String=""
    var strToken:String=""
    private var employeeDetail = ArrayList<ArrEmployeeList>()
    val presenter = EmployeeDetailPresenter(this, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)

       strEmployeeId = intent.getStringExtra("employeeId")!!
        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!

        if(Utilities.checkInternetConnection(this)) {
            presenter.employeeDetails(strToken,strEmployeeId)
            presenter.getReport(strToken,strEmployeeId)

        }else{
            AlertHelper.showNoInternetSnackBar(this@EmployeeDetailActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                    presenter.employeeDetails(strToken,strEmployeeId)
                    presenter.getReport(strToken,strEmployeeId)
                }
            })
        }

        btDeleteUser.setOnClickListener {
            showDeleteDialog()
        }

        btUpdateUser.setOnClickListener {
            val intent = Intent(this, UpdateEmployeeActivity::class.java)
            intent.putExtra("employeeId",strEmployeeId)
            startActivity(intent)
        }




        nav_back.setOnClickListener {
            startActivity(Intent(this@EmployeeDetailActivity,EmployeeListActivity::class.java))
            finish()
        }

        btTargetHistory.setOnClickListener {
            val intent = Intent(this, TargetHistoryActivity::class.java)
            intent.putExtra("employeeId",strEmployeeId)
            startActivity(intent)

        }

        btLocationHistory.setOnClickListener {
            val i = Intent(this, MapActivity::class.java)
            i.putExtra("employeeId",strEmployeeId)
            startActivity(i)
        }

        btTarget.setOnClickListener {
            btUpdateTarget.visibility=View.VISIBLE
            tiTarget.visibility=View.VISIBLE
        }

        btUpdateTarget.setOnClickListener {
            val strTarget=tietTarget.text.toString()
            presenter.createTarget(strToken,strEmployeeId,strTarget.toInt())
        }

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
            Color.rgb(224, 85, 23),
            Color.rgb(242, 242, 242)
        )
        dataSet.colors = COLORFUL_COLORS
        val pieData = PieData(dataSet)
        pieData.setDrawValues(false)

        val str = "${progress.toInt()}%\nCompleted"
        val centeredText = SpannableString(str)
        centeredText.setSpan(RelativeSizeSpan(1.8f), 0, str.indexOf("\n"), 0)

        chartOrders.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
        chartOrders.setCenterTextSize(18f)
        chartOrders.centerText = centeredText
        chartOrders.setUsePercentValues(false)
        chartOrders.setExtraOffsets(5f, 10f, 5f, 5f)
        chartOrders.description.isEnabled = false
        chartOrders.legend.isEnabled = false
        chartOrders.setHoleColor(Color.WHITE)
        chartOrders.holeRadius = 60f
        chartOrders.transparentCircleRadius = 56f
        chartOrders.setDrawEntryLabels(false)
        chartOrders.animateY(1500, Easing.EaseInOutCubic)
        chartOrders.data = pieData
        chartOrders.setTouchEnabled(false)
    }

    override fun onEmployeeDetailSuccess(apiResponse: EmployeeDetailResponse) {
        if(apiResponse!=null) {
            tvName.text = apiResponse.arrList[0].strName
            tvMobile.text = apiResponse.arrList[0].strMobileNo
            tvMailId.text = apiResponse.arrList[0].strEmail
            tvUid.text = apiResponse.arrList[0].strUserId

            if(apiResponse.arrList[0].chrCheckInStatus!=null) {
                if (apiResponse.arrList[0].chrCheckInStatus.equals("I")) {
                    Glide.with(this@EmployeeDetailActivity).load(R.drawable.ic_green_round)
                        .into(ivAttend)
                    tvAttend.text = "Present"
                } else {
                    Glide.with(this@EmployeeDetailActivity).load(R.drawable.ic_red_round)
                        .into(ivAttend)
                    tvAttend.text = "Absent"
                }
            }
        }
    }

    override fun onEmployeeDetailNull(apiResponse: EmployeeDetailResponse) {

    }

    override fun onEmployeeDetailFailed(apiResponse: ResponseBody) {
        Toast.makeText(this,"Data Loading Failed",Toast.LENGTH_SHORT).show()
    }

    override fun onEmployeeDetailFailedServerError(string: String) {
        Toast.makeText(this,"Check your internet connection",Toast.LENGTH_SHORT).show()
    }

    override fun onEmployeeTargetSuccess(apiResponse: DefaultResponse) {
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    override fun onEmployeeTargetNull(apiResponse: DefaultResponse) {
        Toast.makeText(this,"Data Loading Failed",Toast.LENGTH_SHORT).show()
    }

    override fun onEmployeeTargetFailed(apiResponse: ResponseBody) {
        Toast.makeText(this,"Target Update Failed",Toast.LENGTH_SHORT).show()
    }

    override fun onEmployeeTargetFailedServerError(string: String) {
        Toast.makeText(this,"Check your internet connection",Toast.LENGTH_SHORT).show()
    }

    override fun onReportSuccess(apiResponse: ReportResponse) {
        btTarget.text=apiResponse.SALE.dblSaleTarget.toString()
        btTarget.text="${apiResponse.SALE.dblTotalOrderAmount}/${apiResponse.SALE.dblSaleTarget}"

        val target: Long = apiResponse.SALE.dblSaleTarget.toLong()
        val completedTarget: Long = apiResponse.SALE.dblTotalOrderAmount.toLong()

        if (target != 0L) {
            val targetPercentage = (completedTarget * 100) / target
            createTargetChart(targetPercentage.toFloat())
        }
    }

    override fun onReportNull(apiResponse: ReportResponse) {

    }

    override fun onReportFailed(apiResponse: ResponseBody) {

    }

    override fun onReportServerError(string: String) {

    }

    override fun onDeleteUserSuccess(apiResponse: DefaultResponse) {
        Toast.makeText(this,apiResponse.strMessage,Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@EmployeeDetailActivity,EmployeeListActivity::class.java))
        finish()
    }

    override fun onDeleteUserNull(apiResponse: DefaultResponse) {
        Toast.makeText(this,apiResponse.strMessage,Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteUserFailed(apiResponse: JSONArray) {
        AlertHelper.showOKSnackBarAlert(this@EmployeeDetailActivity,apiResponse[0].toString())
        //Toast.makeText(this,apiResponse[0].toString(),Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteUserServerError(string: String) {
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.normal_pop_up_custom)
        val heading = dialog.findViewById<View>(R.id.tvCustomPopupHeading) as TextView
        heading.text = "Are you sure you want to delete"
        val details = dialog.findViewById<View>(R.id.tvCustomPopupDetail) as TextView
        details.text=details.toString()
        details.visibility=View.GONE
        val btYes = dialog.findViewById<View>(R.id.btCustomPopupOk) as Button
        val btNo = dialog.findViewById<View>(R.id.btCustomPopupCancel) as Button
        btYes.setOnClickListener {
            if(Utilities.checkInternetConnection(this)) {
                presenter.deleteUser(strToken,strEmployeeId)

            }else{
                AlertHelper.showNoInternetSnackBar(this@EmployeeDetailActivity, object :
                    AlertHelper.SnackBarListener {
                    override fun onOkClick() {
                        presenter.deleteUser(strToken,strEmployeeId)
                    }
                })
            }
            dialog.dismiss()
        }
        btNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@EmployeeDetailActivity,EmployeeListActivity::class.java))
        finish()

    }
}