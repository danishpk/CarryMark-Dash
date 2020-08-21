package tech.stacka.carrymarkdashboard.activity.order.orderList

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.opencsv.CSVWriter
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.activity_product_list.pbLoadMore
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.home.HomeActivity
import tech.stacka.carrymarkdashboard.activity.order.orderFilter.OrderFilterActivity
import tech.stacka.carrymarkdashboard.adapter.OrderListAdapter
import tech.stacka.carrymarkdashboard.models.OrderListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrOrderList
import tech.stacka.carrymarkdashboard.models.data.ArrProductList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.channels.Channels.newWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class OrderListActivity : AppCompatActivity(), OrderListView {
    private var mTotalItemCount = 0
    private var currentPage=0;
    private var offset=0;
    private var totalPage=0;
    private var limit=20;
    var strToken:String=""
    private var isLoading = false
    private val REQUEST_PERMISSION = 1
    var fromDate:String=""
    var toDate:String=""
    private lateinit var mAdapter: OrderListAdapter
    private var orderList = ArrayList<ArrOrderList>()
    var arrFilteredExeOrder=ArrayList<String>()
    var arrFilteredDistOrder=ArrayList<String>()
    var arrFilteredStatusOrder=ArrayList<String>()
    val presenter=OrderListPresenter(this,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        tv_Nav.text="ORDER LIST"
        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        nav_back.setOnClickListener {
            startActivity(Intent(this@OrderListActivity,HomeActivity::class.java))
            finish()
        }

        btLoadMoreOrder.setOnClickListener {
            pbLoadMore.visibility=View.VISIBLE
            btLoadMoreOrder.visibility=View.GONE
            currentPage++
            if (currentPage <= totalPage) {
                val offsetNw: Int = (currentPage)
                presenter.performPagination(strToken,arrFilteredExeOrder,arrFilteredDistOrder,arrFilteredStatusOrder,offset,limit,fromDate,toDate)
            } else {
                pbLoadMore.visibility=View.GONE
                btLoadMoreOrder.visibility=View.GONE
                Toast.makeText(this@OrderListActivity, "No more orders", Toast.LENGTH_SHORT).show()
            }
        }

        btExportCsv.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this@OrderListActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this@OrderListActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)
            }else {
                val path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS
                )
                val date: String = Date().time.toString()
                val filePath = path.absolutePath + "/" + "orders" + date + ".csv"
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    val writer =  Files.newBufferedWriter(Paths.get(filePath))
//                val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT
//                        .withHeader(
//                            "OrderId",
//                            "OrderDate",
//                            "Amount",
//                            "RetailerID/UserID",
//                            "Name",
//                            "Address",
//                            "Status",
//                            "Products"
//                        )
//                )
//
//                    for (child in orderList) {
//
//                        var product = ""
//                        for (prd in child.arrProducts) {
//                            product +=
//                                prd.strProductId + " Nos. of quantity " + prd.dblQty  + "\n"
//                        }
//
//                        csvPrinter.printRecord(
//                            child.strOrderId.toString(),
//                            child.strCreatedTime.toString(),
//                            child.dblTotalOrderAmount.toString(),
//                            child.strOrderedUserId.toString(),
//                            child.strCreatedUser.toString(),
//                            child.objAddress.strName.toString(),
//                            child.strOrderStatus.toString(),
//                            product
//                        )
//
//                    }
//                    csvPrinter.flush()
//                    csvPrinter.close()
//                 //   AlertHelper.showOKSnackBarAlert(this,"File saved at $filePath")
//                Toast.makeText(this,"File saved at $filePath",Toast.LENGTH_SHORT).show()
//                } else {
//
//                    try {
//
//                        CSVPrinter(FileWriter(File(filePath)), CSVFormat.EXCEL.withHeader(
//                            "OrderId",
//                            "OrderDate",
//                            "Amount",
//                            "RetailerID/UserID",
//                            "Name",
//                            "Address",
//                            "Status",
//                            "Products"
//
//                        )).use { printer ->
//                            for (child in orderList) {
//
//                                var product = ""
//                                for (prd in child.arrProducts) {
//                                    product +=
//                                        prd.strProductId + " Nos. of quantity " + prd.dblQty  + "\n"
//                                }
//
//                                printer.printRecord(
//                                    child.strOrderId.toString(),
//                                    child.strCreatedTime.toString(),
//                                    child.dblTotalOrderAmount.toString(),
//                                    child.strOrderedUserId.toString(),
//                                    child.strCreatedUser.toString(),
//                                    child.objAddress.strName.toString(),
//                                    child.strOrderStatus.toString(),
//                                    product
//                                )
//
//                            }
//                            printer.flush()
//                            printer.close()
//                            Toast.makeText(this,"File saved at $filePath",Toast.LENGTH_SHORT).show()
//                        }
//
//
//                    } catch (ex: IOException) {
//                        ex.printStackTrace()
//                        ActivityCompat.requestPermissions(
//                            this,
//                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                            1
//                        )
//                        AlertHelper.showOKSnackBarAlert(this,"Please Enable Storage Permission\n" + ex.toString())
//                    }
//
//                }
//
//            } catch (e: Exception) {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    1
//                )
//                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
//
//             //   AlertHelper.showOKSnackBarAlert(this,"Please Enable Storage Permission\n" + e.toString())
//
//            }

                try {

                    CSVPrinter(
                        FileWriter(File(filePath)), CSVFormat.EXCEL.withHeader(
                            "Shop Name",
                            "Employee Name",
                            "Order No",
                            "Order Date",
                            "Product Name",
                            "Product Id",
                            "Quantity",
                            "Rate(with out tax)",
                            "SGST",
                            "CGST",
                            "IGST",
                            "Total Amount"

                        )
                    ).use { printer ->
                        for (child in orderList) {

                            var product = ""
                            for (prd in child.arrProducts) {

                                var igst=0.0
                                val strState=child.strState
                                if(strState=="KERALA"){
                                    igst=0.0
                                }else if(strState==""){
                                    igst=0.0
                                }else{
                                    igst=prd.dblCGSTPrice+prd.dblSGSTPrice
                                }

//                                    product +=
//                                        prd.strProductId + " Nos. of quantity " + prd.dblQty  + "\n"

                                printer.printRecord(
                                    child.strShopName.toString(),
                                    child.strExecutiveName.toString(),
                                    child.strOrderId.toString(),
                                    child.strCSVDate.toString(),
                                    prd.strName.toString(),
                                    prd.strOGProductId.toString(),
                                    prd.dblQty.toString(),
                                    prd.dblOGPrice.toString(),
                                    prd.dblSGSTPrice.toString(),
                                    prd.dblCGSTPrice.toString(),
                                    igst.toString(),
                                    prd.dblBETotalAmount.toString()

                                )
                            }

//                                printer.printRecord(
//                                    child.strOrderId.toString(),
//                                    child.strCreatedTime.toString(),
//                                    child.dblTotalOrderAmount.toString(),
//                                    child.strOrderedUserId.toString(),
//                                    child.strCreatedUser.toString(),
//                                    child.objAddress.strName.toString(),
//                                    child.strOrderStatus.toString(),
//                                    product
//                                )

                        }
                        printer.flush()
                        printer.close()
                        Toast.makeText(this, "File saved at $filePath", Toast.LENGTH_SHORT).show()
                    }


                } catch (ex: IOException) {
                    ex.printStackTrace()
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )
                    AlertHelper.showOKSnackBarAlert(
                        this,
                        "Please Enable Storage Permission\n" + ex.toString()
                    )
                }
            }

        }

        //Intent from Order Filter
        val bundle: Bundle? = intent.extras
        if(intent.extras!=null){
            arrFilteredExeOrder = intent.getStringArrayListExtra("arrSelExeIds")
            arrFilteredDistOrder = intent.getStringArrayListExtra("arrSelDistIds")
            arrFilteredStatusOrder = intent.getStringArrayListExtra("arrSelStatus")
            fromDate=intent.getStringExtra("fromDate")
            toDate=intent.getStringExtra("toDate")
        }
        tvFilterOrderlist.setOnClickListener {
            intent= Intent(applicationContext, OrderFilterActivity::class.java)
            intent.putExtra("arrFilteredExeOrder",arrFilteredExeOrder as ArrayList<String>)
            intent.putExtra("arrFilteredDistOrder",arrFilteredDistOrder as ArrayList<String>)
            intent.putExtra("arrFilteredStatusOrder",arrFilteredStatusOrder as ArrayList<String>)
            intent.putExtra("fromDate",fromDate)
            intent.putExtra("toDate",toDate)
            startActivity(intent)
        }

        if(Utilities.checkInternetConnection(this)) {
            presenter.orderList(strToken,arrFilteredExeOrder,arrFilteredDistOrder,arrFilteredStatusOrder,offset,limit,fromDate,toDate)
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
        mTotalItemCount=apiResponse.intTotalCount
        totalPage=mTotalItemCount/limit
        if(apiResponse.intTotalCount>limit){
            btLoadMoreOrder.visibility=View.VISIBLE
        }
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

    override fun onOrderListMoreSuccess(apiResponse: OrderListResponse) {
        orderList= apiResponse.arrList as ArrayList<ArrOrderList>
        mAdapter.addAll(orderList)
        pbLoadMore.visibility=View.GONE
        btLoadMoreOrder.visibility = View.VISIBLE
        if(currentPage==totalPage){
            btLoadMoreOrder.visibility=View.GONE
          //  tvNoMoreOrder.visibility=View.VISIBLE
        }
    }

    override fun onOrderListMoreNull(apiResponse: OrderListResponse) {
        pbLoadMore.visibility=View.GONE
        btLoadMoreOrder.visibility = View.VISIBLE
    }

    override fun onOrderListMoreFailed(apiResponse: ResponseBody) {
        pbLoadMore.visibility=View.GONE
        btLoadMoreOrder.visibility = View.VISIBLE
    }

    override fun onOrderListMoreServerFailed(toString: String) {
        pbLoadMore.visibility=View.GONE
        btLoadMoreOrder.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@OrderListActivity,HomeActivity::class.java))
        finish()
    }
}