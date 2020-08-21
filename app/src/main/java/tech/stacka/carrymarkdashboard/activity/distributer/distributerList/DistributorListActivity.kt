package tech.stacka.carrymarkdashboard.activity.distributer.distributerList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_distributor_list.*
import kotlinx.android.synthetic.main.activity_distributor_list.noDataLayoutSubject
import kotlinx.android.synthetic.main.activity_employee_list.pbLoadMore
import kotlinx.android.synthetic.main.activity_employee_list.pbLoading
import kotlinx.android.synthetic.main.activity_retailer_list.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.home.HomeActivity
import tech.stacka.carrymarkdashboard.adapter.DistributorListAdapter
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.DistributerListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrDistributerList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities

class DistributorListActivity : AppCompatActivity(), DistributorListView {

    private var mTotalItemCount = 0
    private var mLastVisibleItemPosition: Int = 0
    private val mPostsPerPage = 10L
    private var isAvailable = true
    private var isLoading = false
    private var selectUser = false
    private lateinit var mAdapter: DistributorListAdapter
    private var distributerList = ArrayList<ArrDistributerList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distributor_list)

        nav_back.setOnClickListener {
            startActivity(Intent(this@DistributorListActivity,HomeActivity::class.java))
            finish()
        }
        val strToken:String= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        val presenter = DistributorListPresenter(this, this)

        if(Utilities.checkInternetConnection(this)) {
            presenter.distributerList(strToken)

        }else{
            AlertHelper.showNoInternetSnackBar(this@DistributorListActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                    presenter.distributerList(strToken)
                }
            })
        }

    }

    override fun onDistributerListSuccess(apiResponse: DistributerListResponse) {
        val strLastItemCount:String=apiResponse.intTotalCount.toString();
        distributerList= apiResponse.arrList as ArrayList<ArrDistributerList>
        selectUser = intent.getBooleanExtra("selectUser", false)
        val mLayoutManager = LinearLayoutManager(this)
        rvDistributor.layoutManager = mLayoutManager
        rvDistributor.setHasFixedSize(true)
        mAdapter = DistributorListAdapter(applicationContext,distributerList,selectUser,this)
        //   mAdapter.addAll(employeeList)
        rvDistributor.adapter = mAdapter
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        isLoading = false
        if(apiResponse.arrList.isEmpty()){
            noDataLayoutSubject.visibility=View.VISIBLE
        }
    }

    override fun onDistributerListFailed(apiResponse: ResponseBody) {
        AlertHelper.showOKSnackBarAlert(this@DistributorListActivity,"Distributor List Failed")
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
    }

    override fun onDistributerListFailedServerError(string: String) {
        AlertHelper.showOKSnackBarAlert(this@DistributorListActivity,"Check internet connection")
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
    }

    override fun onDistributerListNull(apiResponse: DistributerListResponse) {
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@DistributorListActivity,HomeActivity::class.java))
        finish()
    }
}