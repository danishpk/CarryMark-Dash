package tech.stacka.carrymarkdashboard.activity.retailer.retailerList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_retailer_list.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.home.HomeActivity
import tech.stacka.carrymarkdashboard.adapter.RetailerListAdapter
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.DistributerListResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.RetailerListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrDistributerList
import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList
import tech.stacka.carrymarkdashboard.models.data.ArrRetailerList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities

class RetailerListActivity : AppCompatActivity(), RetailerListView,
    RetailerListAdapter.DataTransferInterfaceRetailer {
    private lateinit var mAdapter: RetailerListAdapter
    //private lateinit var lastSnap: QueryDocumentSnapshot
    private var retailersList = ArrayList<ArrRetailerList>()
    private var mLastVisibleItemPosition: Int = 0
    private val mPostsPerPage = 5L
    private var isAvailable = true
    private var isLoading = false
    private var selectUser = false
    var strToken:String=""
    var EMPLOYEE_LOAD:Int=0
    var DISTRIBUTER_LOAD:Int=0
    var strSearchKey:String=""
    var SEARCH_VIS_FLAG=false
    var SEARCHED_FLAG=false
    private var currentPage=0;
    private var offset=0;
    private var totalPage=0;
    private var limit=20;
    private var mTotalItemCount = 0
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
            startActivity(Intent(this@RetailerListActivity,HomeActivity::class.java))
            finish()
        }

        btLoadMore.setOnClickListener {
            pbLoadMore.visibility=View.VISIBLE
            btLoadMore.visibility=View.GONE
            currentPage++
            if (currentPage <= totalPage) {
                val offsetNw: Int = (currentPage)
                presenter.retailerPagination(strToken, offsetNw,limit)
            } else {
                pbLoadMore.visibility=View.GONE
                btLoadMore.visibility=View.GONE
                Toast.makeText(this@RetailerListActivity, "No more retailers", Toast.LENGTH_SHORT).show()
            }
        }

        tv_Nav.text = "Retailers"

        searchButton.visibility=View.VISIBLE
        searchButton.setOnClickListener {
            if(SEARCH_VIS_FLAG){
                searchRow.visibility=View.GONE
                SEARCH_VIS_FLAG=false
                if(SEARCHED_FLAG){
                    pbLoading.visibility = View.VISIBLE
                    if (Utilities.checkInternetConnection(this)) {
                        presenter.retailerList(strToken,offset,limit)
                    } else {
                        Toast.makeText(this, "check your internet connection", Toast.LENGTH_LONG).show()
                    }
                    SEARCHED_FLAG=false
                }

            }else{
                searchRow.visibility=View.VISIBLE
                SEARCH_VIS_FLAG=true

            }
        }

        btSearch.setOnClickListener {

            strSearchKey=etSearch.text.toString().trim()
            pbLoading.visibility = View.VISIBLE
            if(Utilities.checkInternetConnection(this)) {
                presenter.searchList(strToken,strSearchKey,offset,limit)
            }else{
                Toast.makeText(this,"check your internet connection", Toast.LENGTH_LONG).show()
            }
        }


        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        if (Utilities.checkInternetConnection(this)) {
            presenter.retailerList(strToken,offset,limit)
        } else {
            Toast.makeText(this, "check your internet connection", Toast.LENGTH_LONG).show()
        }


    }

    override fun onRetailerListSuccess(apiResponse: RetailerListResponse) {
        noDataLayoutSubject.visibility=View.GONE
        retailersList= apiResponse.arrList as ArrayList<ArrRetailerList>
        mTotalItemCount=apiResponse.intTotalCount
        totalPage=mTotalItemCount/limit
        Log.i("totalPage",totalPage.toString())
        presenter.employeeList(strToken)
        val strLastItemCount:String=apiResponse.intTotalCount.toString();

        if(apiResponse.arrList.isEmpty()){
            noDataLayoutSubject.visibility=View.VISIBLE
        }




    }

    override fun onRetailerListFailed(apiResponse: ResponseBody) {
        pbLoading.visibility = View.GONE
    }

    override fun onRetailerListFailedServerError(string: String) {
        pbLoading.visibility = View.GONE
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }

    override fun onRetailerListNull(apiResponse: RetailerListResponse) {
        pbLoading.visibility = View.GONE
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

    }

    override fun onEmployeeListFailed(apiResponse: ResponseBody) {
       Toast.makeText(applicationContext,"List failed",Toast.LENGTH_SHORT).show()
    }

    override fun onEmployeeListFailedServerError(string: String) {
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }

    override fun onDistributerListNull(apiResponse: DistributerListResponse) {

    }

    override fun onDistributerListSuccess(apiResponse: DistributerListResponse) {
        distributerList= apiResponse.arrList as ArrayList<ArrDistributerList>
        for(i in distributerList){
            arrDistributerNameList.add(i.strName)
            arrDistributerIdList.add(i._id)

        }
        Log.i("TotalItem",mTotalItemCount.toString())
      //  DISTRIBUTER_LOAD=1
        if(mTotalItemCount>limit){
            btLoadMore.visibility=View.VISIBLE
        }
        selectUser = intent.getBooleanExtra("selectUser", false)
        val mLayoutManager = LinearLayoutManager(this)
        rvRetailers.layoutManager = mLayoutManager
        rvRetailers.setHasFixedSize(true)
        mAdapter = RetailerListAdapter(this,retailersList,selectUser,this,arrEmployeeNameList,arrEmployeeIdList,
            arrDistributerNameList,arrDistributerIdList,this)
        //   mAdapter.addAll(employeeList)
        rvRetailers.adapter = mAdapter
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        isLoading = false
    }

    override fun onDistributerListFailed(apiResponse: ResponseBody) {
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        Toast.makeText(applicationContext,"List failed",Toast.LENGTH_SHORT).show()
    }

    override fun onDistributerListFailedServerError(string: String) {
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }

    override fun onRetailerUpdateSuccess(apiResponse: DefaultResponse) {
        AlertHelper.showOKSnackBarAlert(this@RetailerListActivity,"Retailer successfully updated")
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    override fun onRetailerUpdateNull(apiResponse: DefaultResponse) {
     //   AlertHelper.showOKSnackBarAlert(this@RetailerListActivity,"Retailer successfully updated")
    }

    override fun onRetailerUpdateFailed(apiResponse: ResponseBody) {
        AlertHelper.showOKSnackBarAlert(this@RetailerListActivity,"Retailer update failed")
    }

    override fun onRetailerUpdateFailedServerError(string: String) {
        AlertHelper.showOKSnackBarAlert(this@RetailerListActivity,"Network Problem")
    }

    override fun onRetailerSearchListSuccess(apiResponse: RetailerListResponse) {
        SEARCHED_FLAG=true
        btLoadMore.visibility=View.GONE
        noDataLayoutSubject.visibility=View.GONE
        val strLastItemCount:String=apiResponse.intTotalCount.toString();
        retailersList= apiResponse.arrList as ArrayList<ArrRetailerList>
        selectUser = intent.getBooleanExtra("selectUser", false)
        val mLayoutManager = LinearLayoutManager(this)
        rvRetailers.layoutManager = mLayoutManager
        rvRetailers.setHasFixedSize(true)
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        if(apiResponse.arrList.isEmpty()){
            noDataLayoutSubject.visibility=View.VISIBLE
            rvRetailers.visibility=View.GONE
        }else{
            rvRetailers.visibility=View.VISIBLE
            noDataLayoutSubject.visibility=View.GONE
            mAdapter = RetailerListAdapter(this,retailersList,selectUser,this,arrEmployeeNameList,arrEmployeeIdList,
                arrDistributerNameList,arrDistributerIdList,this)
            //   mAdapter.addAll(employeeList)
            rvRetailers.adapter = mAdapter

        }
    }

    override fun onRetailerSearchListNull(apiResponse: RetailerListResponse) {
        pbLoading.visibility = View.GONE
        SEARCHED_FLAG=true
    }

    override fun onRetailerSearchListFailed(apiResponse: ResponseBody) {
        pbLoading.visibility = View.GONE
        SEARCHED_FLAG=true
    }

    override fun onRetailerListSearchFailedServerError(string: String) {
        pbLoading.visibility = View.GONE
        SEARCHED_FLAG=true
    }

    override fun onRetailerListMoreSuccess(apiResponse: RetailerListResponse) {

        retailersList= apiResponse.arrList as ArrayList<ArrRetailerList>
        mAdapter.addAll(retailersList)
        Log.i("CurentPageMore",currentPage.toString())
        btLoadMore.visibility=View.VISIBLE
        if(currentPage==totalPage){
            btLoadMore.visibility=View.GONE
            //  tvNoMoreOrder.visibility=View.VISIBLE
        }
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
    }

    override fun onRetailerListMOreNull(apiResponse: RetailerListResponse) {
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
    }

    override fun onRetailerListMoreFailed(apiResponse: ResponseBody) {
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
       AlertHelper.showOKSnackBarAlert(this@RetailerListActivity,apiResponse.toString())
    }

    override fun onRetailerListFailedMoreServerError(string: String) {
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        AlertHelper.showNoInternetSnackBar(this@RetailerListActivity, object :
            AlertHelper.SnackBarListener {
            override fun onOkClick() {
            }
        })
    }

    override fun setRetailerValues(
        strExecutiveId: String,
        strExecutiveName: String,
        strDistributorId: String,
        strDistributorName: String,
        strActive: String,
        strRetailerId:String,
        strDiscount:String
    ) {
        presenter.updateRetailer(strToken,strExecutiveId,strExecutiveName,strDistributorId,strDistributorName,strActive,strRetailerId,strDiscount)
    }

    override fun onBackPressed() {
        if(SEARCH_VIS_FLAG){
            searchRow.visibility=View.GONE
            SEARCH_VIS_FLAG=false
            if(SEARCHED_FLAG) {
                pbLoading.visibility = View.VISIBLE
                if (Utilities.checkInternetConnection(this)) {
                    presenter.retailerList(strToken,offset,limit)
                } else {

                    Toast.makeText(this, "check your internet connection", Toast.LENGTH_LONG).show()
                }
                SEARCHED_FLAG=false
            }
        }else{
            super.onBackPressed()
            startActivity(Intent(this@RetailerListActivity, HomeActivity::class.java))
            finish()
        }
    }
}