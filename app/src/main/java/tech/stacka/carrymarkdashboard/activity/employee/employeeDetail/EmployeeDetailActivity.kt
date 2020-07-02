package tech.stacka.carrymarkdashboard.activity.employee.employeeDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_employee_detail.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.employee.employeeList.EmployeeListPresenter
import tech.stacka.carrymarkdashboard.activity.employee.employeeList.EmployeeListView
import tech.stacka.carrymarkdashboard.models.EmployeeDetailResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager

class EmployeeDetailActivity : AppCompatActivity(), EmployeeDetailView {
    private var employeeDetail = ArrayList<ArrEmployeeList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)
       val strEmployeeId = intent.getStringExtra("employeeId")!!
        val strToken:String= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        val presenter = EmployeeDetailPresenter(this, this)
        presenter.employeeDetails(strToken,strEmployeeId)

        nav_back.setOnClickListener {
            finish()
        }
    }

    override fun onEmployeeDetailSuccess(apiResponse: EmployeeDetailResponse) {
        tvName.text = apiResponse.arrList[0].strName
        tvMobile.text = apiResponse.arrList[0].strMobileNo
        tvMailId.text = apiResponse.arrList[0].strEmail
    }

    override fun onEmployeeDetailNull(apiResponse: EmployeeDetailResponse) {
        TODO("Not yet implemented")
    }

    override fun onEmployeeDetailFailed(apiResponse: ResponseBody) {
        TODO("Not yet implemented")
    }

    override fun onEmployeeDetailFailedServerError(string: String) {
        TODO("Not yet implemented")
    }


}