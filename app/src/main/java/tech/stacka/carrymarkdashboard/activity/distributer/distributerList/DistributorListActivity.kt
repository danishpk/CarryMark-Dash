package tech.stacka.carrymarkdashboard.activity.distributer.distributerList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_distributor_list.*
import kotlinx.android.synthetic.main.activity_employee_list.pbLoadMore
import kotlinx.android.synthetic.main.activity_employee_list.pbLoading
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.adapter.DistributorListAdapter
import tech.stacka.carrymarkdashboard.models.DistributerListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrDistributerList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager

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
            finish()
        }
        val strToken:String= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        val presenter = DistributorListPresenter(this, this)
        presenter.distributerList(strToken)
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
    }

    override fun onDistributerListFailed(apiResponse: ResponseBody) {
        TODO("Not yet implemented")
    }

    override fun onDistributerListFailedServerError(string: String) {
        TODO("Not yet implemented")
    }

    override fun onDistributerListNull(apiResponse: DistributerListResponse) {
        TODO("Not yet implemented")
    }
}