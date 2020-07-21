package tech.stacka.carrymarkdashboard.activity.employee.employeeDetail

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.activity_employee_detail.*

import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.map.MapActivity
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.EmployeeDetailResponse
import tech.stacka.carrymarkdashboard.models.ReportResponse
import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EmployeeDetailActivity : AppCompatActivity(), EmployeeDetailView {
    private var employeeDetail = ArrayList<ArrEmployeeList>()
    val presenter = EmployeeDetailPresenter(this, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)
       val strEmployeeId = intent.getStringExtra("employeeId")!!
        val strToken:String= SharedPrefManager.getInstance(applicationContext).user.strToken!!

        presenter.employeeDetails(strToken,strEmployeeId)
        presenter.getReport(strToken,strEmployeeId)


        nav_back.setOnClickListener {
            finish()
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

//        var target: Long = 15
//        var completedTarget: Long = 6
//
//        if (target != 0L) {
//            val targetPercentage = (completedTarget * 100) / target
//            createTargetChart(targetPercentage.toFloat())
//        }
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
        tvName.text = apiResponse.arrList[0].strName
        tvMobile.text = apiResponse.arrList[0].strMobileNo
        tvMailId.text = apiResponse.arrList[0].strEmail
        tvUid.text=apiResponse.arrList[0]._id


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

        val target: Long = apiResponse.SALE.dblSaleTarget.toLong()
        val completedTarget: Long = apiResponse.SALE.dblTotalOrderAmount.toLong()

        if (target != 0L) {
            val targetPercentage = (completedTarget * 100) / target
            createTargetChart(targetPercentage.toFloat())
        }
    }

    override fun onReportNull(apiResponse: ReportResponse) {
        TODO("Not yet implemented")
    }

    override fun onReportFailed(apiResponse: ResponseBody) {
        TODO("Not yet implemented")
    }

    override fun onReportServerError(string: String) {
        TODO("Not yet implemented")
    }


}