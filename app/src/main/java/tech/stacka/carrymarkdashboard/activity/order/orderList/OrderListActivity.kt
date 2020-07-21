package tech.stacka.carrymarkdashboard.activity.order.orderList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.activity_product_list.pbLoadMore
import kotlinx.android.synthetic.main.activity_product_list.pbLoading
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.home.HomeActivity
import tech.stacka.carrymarkdashboard.activity.order.orderFilter.OrderFilterActivity
import tech.stacka.carrymarkdashboard.activity.product.filterProduct.ProductFilterActivity
import tech.stacka.carrymarkdashboard.adapter.OrderListAdapter
import tech.stacka.carrymarkdashboard.models.OrderListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrOrderList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities

class OrderListActivity : AppCompatActivity(), OrderListView {
    var strToken:String=""
    private var isLoading = false
    private lateinit var mAdapter: OrderListAdapter
    private var orderList = ArrayList<ArrOrderList>()
    var arrFilteredExeOrder=ArrayList<String>()
    var arrFilteredDistOrder=ArrayList<String>()
    var arrFilteredStatusOrder=ArrayList<String>()
    val presenter=OrderListPresenter(this,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        nav_back.setOnClickListener {
            startActivity(Intent(this@OrderListActivity,HomeActivity::class.java))
            finish()
        }
        //Intent from Order Filter
        val bundle: Bundle? = intent.extras
        if(intent.extras!=null){
            arrFilteredExeOrder = intent.getStringArrayListExtra("arrSelExeIds")
            arrFilteredDistOrder = intent.getStringArrayListExtra("arrSelDistIds")
            arrFilteredStatusOrder = intent.getStringArrayListExtra("arrSelStatus")
        }
        tvFilterOrderlist.setOnClickListener {
            intent= Intent(applicationContext, OrderFilterActivity::class.java)
            intent.putExtra("arrFilteredExeOrder",arrFilteredExeOrder as ArrayList<String>)
            intent.putExtra("arrFilteredDistOrder",arrFilteredDistOrder as ArrayList<String>)
            intent.putExtra("arrFilteredStatusOrder",arrFilteredStatusOrder as ArrayList<String>)
            startActivity(intent)
        }

        if(Utilities.checkInternetConnection(this)) {
            presenter.orderList(strToken,arrFilteredExeOrder,arrFilteredDistOrder,arrFilteredStatusOrder)
        }else{
            pbLoadingOrder.visibility=View.GONE
            pbLoadMore.visibility=View.GONE
            isLoading = false
            AlertHelper.showNoInternetSnackBar(this@OrderListActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                  //  presenter.orderList(strToken,arrFilteredExeOrder,arrFilteredDistOrder,arrFilteredStatusOrder)
                }
            })
        }



    }

    override fun onOrderListSuccess(apiResponse: OrderListResponse) {
        Log.e("my response ",apiResponse.toString())
        orderList= apiResponse.arrList as ArrayList<ArrOrderList>
        if(orderList.isEmpty()){
            layoutOrderEmpty.visibility=View.VISIBLE
        }
        val mLayoutManager = GridLayoutManager(this, 2)
        rvOrderList.layoutManager = mLayoutManager
        rvOrderList.setHasFixedSize(true)
        mAdapter = OrderListAdapter(applicationContext,orderList)
        rvOrderList.adapter = mAdapter
        isLoading = false
        pbLoadingOrder.visibility=View.GONE
        pbLoadMore.visibility=View.GONE
    }

    override fun onOrderListNull(apiResponse: OrderListResponse) {
        pbLoadingOrder.visibility=View.GONE
        pbLoadMore.visibility=View.GONE

    }

    override fun onOrderListFailed(apiResponse: ResponseBody) {
        pbLoadingOrder.visibility=View.GONE
        pbLoadMore.visibility=View.GONE

    }

    override fun onOrderServerFailed(toString: String) {
        pbLoadingOrder.visibility=View.GONE
        pbLoadMore.visibility=View.GONE

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@OrderListActivity,HomeActivity::class.java))
        finish()
    }
}