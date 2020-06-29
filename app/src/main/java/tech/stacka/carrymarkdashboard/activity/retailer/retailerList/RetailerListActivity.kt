package tech.stacka.carrymarkdashboard.activity.retailer.retailerList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_distributor_list.pbLoadMore
import kotlinx.android.synthetic.main.activity_distributor_list.pbLoading
import kotlinx.android.synthetic.main.activity_retailer_list.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.adapter.RetailerListAdapter
import tech.stacka.carrymarkdashboard.models.RetailerListResponse
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
    //val db = FirebaseFirestore.getInstance()
    private var executiveList = ArrayList<String>()
    private var executiveIdList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retailer_list)

        nav_back.setOnClickListener {
            finish()
        }

        tv_Nav.text = "Retailers"
        nav_back.setOnClickListener { finish() }

        val strToken:String= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        val presenter = RetailerListPresenter(this, this)
        presenter.retailerList(strToken)
    }

    override fun onRetailerListSuccess(apiResponse: RetailerListResponse) {
        val strLastItemCount:String=apiResponse.intTotalCount.toString();
        retailersList= apiResponse.arrList as ArrayList<ArrRetailerList>
        selectUser = intent.getBooleanExtra("selectUser", false)
        val mLayoutManager = LinearLayoutManager(this)
        rvRetailers.layoutManager = mLayoutManager
        rvRetailers.setHasFixedSize(true)
        mAdapter = RetailerListAdapter(applicationContext,retailersList,selectUser,this,executiveList,executiveIdList)
        //   mAdapter.addAll(employeeList)
        rvRetailers.adapter = mAdapter
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        isLoading = false
    }

    override fun onRetailerListFailed(apiResponse: ResponseBody) {
        TODO("Not yet implemented")
    }

    override fun onRetailerListFailedServerError(string: String) {
        TODO("Not yet implemented")
    }

    override fun onRetailerListNull(apiResponse: RetailerListResponse) {
        TODO("Not yet implemented")
    }
}