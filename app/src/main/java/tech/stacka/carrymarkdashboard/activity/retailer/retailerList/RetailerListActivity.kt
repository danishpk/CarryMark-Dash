package tech.stacka.carrymarkdashboard.activity.retailer.retailerList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_distributor_list.pbLoadMore
import kotlinx.android.synthetic.main.activity_distributor_list.pbLoading
import kotlinx.android.synthetic.main.activity_retailer_list.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.adapter.RetailerListAdapter
import tech.stacka.carrymarkdashboard.models.DistributerListResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.RetailerListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrDistributerList
import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList
import tech.stacka.carrymarkdashboard.models.data.ArrRetailerList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager

class RetailerListActivity : AppCompatActivity(), RetailerListView {
    private lateinit var mAdapter: RetailerListAdapter
    //private lateinit var lastSnap: QueryDocumentSnapshot
    private var retailersList = ArrayList<ArrRetailerList>()
    private var mTotalItemCount = 0
    private var mLastVisibleItemPosition: Int = 0
    private val mPostsPerPage = 5L
    private var isAvailable = true
    private var isLoading = false
    private var selectUser = false
    var strToken:String=""
    var EMPLOYEE_LOAD:Int=0
    var DISTRIBUTER_LOAD:Int=0

    //val db = FirebaseFirestore.getInstance()
    private var arrEmployeeNameList = ArrayList<String>()
    private var arrEmployeeIdList = ArrayList<String>()
    private var employeeList = ArrayList<ArrEmployeeList>()
    private var distributerList = ArrayList<ArrDistributerList>()
    private var arrDistributerNameList = ArrayList<String>()
    private var arrDistributerIdList = ArrayList<String>()
    val presenter = RetailerListPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retailer_list)

        nav_back.setOnClickListener {
            finish()
        }

        tv_Nav.text = "Retailers"
        nav_back.setOnClickListener { finish() }

        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        presenter.retailerList(strToken)


    }

    override fun onRetailerListSuccess(apiResponse: RetailerListResponse) {
        presenter.employeeList(strToken)
        val strLastItemCount:String=apiResponse.intTotalCount.toString();
        retailersList= apiResponse.arrList as ArrayList<ArrRetailerList>




    }

    override fun onRetailerListFailed(apiResponse: ResponseBody) {
        TODO("Not yet implemented")
    }

    override fun onRetailerListFailedServerError(string: String) {
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }

    override fun onRetailerListNull(apiResponse: RetailerListResponse) {
        TODO("Not yet implemented")
    }

    override fun onEmployeeListSuccess(apiResponse: EmployeeListResponse) {
        employeeList= apiResponse.arrList as ArrayList<ArrEmployeeList>
        for(i in employeeList){
            arrEmployeeNameList.add(i.strName)
            arrEmployeeIdList.add(i._id)
        }
        presenter.distributerList(strToken)
            //   EMPLOYEE_LOAD=1;
    }

    override fun onEmployeeListNull(apiResponse: EmployeeListResponse) {
        TODO("Not yet implemented")
    }

    override fun onEmployeeListFailed(apiResponse: ResponseBody) {
       Toast.makeText(applicationContext,"List failed",Toast.LENGTH_SHORT).show()
    }

    override fun onEmployeeListFailedServerError(string: String) {
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }

    override fun onDistributerListNull(apiResponse: DistributerListResponse) {
        TODO("Not yet implemented")
    }

    override fun onDistributerListSuccess(apiResponse: DistributerListResponse) {
        distributerList= apiResponse.arrList as ArrayList<ArrDistributerList>
        for(i in distributerList){
            arrDistributerNameList.add(i.strName)
            arrDistributerIdList.add(i._id)

        }

      //  DISTRIBUTER_LOAD=1

        selectUser = intent.getBooleanExtra("selectUser", false)
        val mLayoutManager = LinearLayoutManager(this)
        rvRetailers.layoutManager = mLayoutManager
        rvRetailers.setHasFixedSize(true)
        mAdapter = RetailerListAdapter(this,retailersList,selectUser,this,arrEmployeeNameList,arrEmployeeIdList,
            arrDistributerNameList,arrDistributerIdList)
        //   mAdapter.addAll(employeeList)
        rvRetailers.adapter = mAdapter
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        isLoading = false
    }

    override fun onDistributerListFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,"List failed",Toast.LENGTH_SHORT).show()
    }

    override fun onDistributerListFailedServerError(string: String) {
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }
}