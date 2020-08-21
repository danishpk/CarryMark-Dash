package tech.stacka.carrymarkdashboard.activity.employee.employeeList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_employee_list.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.home.HomeActivity
import tech.stacka.carrymarkdashboard.adapter.ExecutiveListAdapter
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities

class EmployeeListActivity : AppCompatActivity(), EmployeeListView {
    var employeeList = ArrayList<ArrEmployeeList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_list)

        nav_back.setOnClickListener {
            startActivity(Intent(this@EmployeeListActivity,HomeActivity::class.java))
            finish()
        }
        val strToken:String= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        val presenter = EmployeeListPresenter(this, this)


        if(Utilities.checkInternetConnection(this)) {
            presenter.employeeList(strToken)

        }else{
            AlertHelper.showNoInternetSnackBar(this@EmployeeListActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                    presenter.employeeList(strToken)
                }
            })
        }




    }

    override fun onEmployeeListSuccess(apiResponse: EmployeeListResponse) {
        employeeList= apiResponse.arrList as ArrayList<ArrEmployeeList>
        val mLayoutManager = LinearLayoutManager(this)
        rvEmpList.layoutManager = mLayoutManager
        rvEmpList.setHasFixedSize(true)
        val mAdapterData = ExecutiveListAdapter(this@EmployeeListActivity,employeeList)
        rvEmpList.adapter = mAdapterData
        pbLoading.visibility = View.GONE
        if(apiResponse.arrList.isEmpty()){
            noDataLayoutSubject.visibility=View.VISIBLE
        }
    }

    override fun onEmployeeListFailed(apiResponse: ResponseBody) {
        AlertHelper.showOKSnackBarAlert(this@EmployeeListActivity,"Employee List Failed")
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
    }

    override fun onEmployeeListFailedServerError(string: String) {
        AlertHelper.showNoInternetSnackBar(this@EmployeeListActivity, object :
            AlertHelper.SnackBarListener {
            override fun onOkClick() {
            }
        })
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
    }

    override fun onEmployeeListNull(apiResponse: EmployeeListResponse) {
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@EmployeeListActivity,HomeActivity::class.java))
        finish()
    }
}