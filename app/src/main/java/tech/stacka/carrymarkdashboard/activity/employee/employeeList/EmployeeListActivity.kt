package tech.stacka.carrymarkdashboard.activity.employee.employeeList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_employee_list.*
import kotlinx.android.synthetic.main.activity_product_list.pbLoadMore
import kotlinx.android.synthetic.main.activity_product_list.pbLoading
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.adapter.EmployeeListAdapter
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager

class EmployeeListActivity : AppCompatActivity(), EmployeeListView {
    private var mTotalItemCount = 0
    private var mLastVisibleItemPosition: Int = 0
    private val mPostsPerPage = 10L
    private var isAvailable = true
    private var isLoading = false
    private var selectUser = false
    private lateinit var mAdapter: EmployeeListAdapter
    private var employeeList = ArrayList<ArrEmployeeList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_list)

        nav_back.setOnClickListener {
            finish()
        }
        val strToken:String= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        val presenter = EmployeeListPresenter(this, this)
        presenter.employeeList(strToken)






    }

    override fun onEmployeeListSuccess(apiResponse: EmployeeListResponse) {
        val strLastItemCount:String=apiResponse.intTotalCount.toString();
        employeeList= apiResponse.arrList as ArrayList<ArrEmployeeList>
        selectUser = intent.getBooleanExtra("selectUser", false)
        val mLayoutManager = LinearLayoutManager(this)
        rvEmpList.layoutManager = mLayoutManager
        rvEmpList.setHasFixedSize(true)
        mAdapter = EmployeeListAdapter(applicationContext,employeeList,this,selectUser)
         //   mAdapter.addAll(employeeList)
        rvEmpList.adapter = mAdapter
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        isLoading = false
    }

    override fun onEmployeeListFailed(apiResponse: ResponseBody) {

    }

    override fun onEmployeeListFailedServerError(string: String) {

    }

    override fun onEmployeeListNull(apiResponse: EmployeeListResponse) {
        TODO("Not yet implemented")
    }
}