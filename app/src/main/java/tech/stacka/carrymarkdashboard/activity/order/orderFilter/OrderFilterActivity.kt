package tech.stacka.carrymarkdashboard.activity.order.orderFilter

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_order_filter.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.MainActivity
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.order.orderList.OrderListActivity
import tech.stacka.carrymarkdashboard.activity.product.productList.ProductListActivity
import tech.stacka.carrymarkdashboard.adapter.FilterCategoryAdapter
import tech.stacka.carrymarkdashboard.adapter.FilterOrderDistributerAdapter
import tech.stacka.carrymarkdashboard.adapter.FilterOrderExecutiveAdapter
import tech.stacka.carrymarkdashboard.models.DistributerListResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrDistributerList
import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderFilterActivity : AppCompatActivity(), OrderFilterView,
    FilterCategoryAdapter.DataTransferInterface, FilterOrderExecutiveAdapter.DataTransferInterface,
    FilterOrderDistributerAdapter.DataTransferInterface {

    var arrSelectedStatusWise = ArrayList<String>()
    var arrSelExeIds=ArrayList<String>()
    var arrSelDistIds=ArrayList<String>()
    var fromDate:String=""
    var toDate:String=""
    private lateinit var fromDatePicker: DatePickerDialog
    private lateinit var toDatePicker: DatePickerDialog
    var arrDistributorIds=ArrayList<String>()
    var arrDistributorName=ArrayList<String>()
    var arrDistributorDetail=ArrayList<ArrDistributerList>()
    var arrExecutiveIds=ArrayList<String>()
    var arrExecutiveName=ArrayList<String>()
    var arrExecutiveDetail=ArrayList<ArrEmployeeList>()
    var strToken:String=""
    val presenter=OrderFilterPresenter(this@OrderFilterActivity,this@OrderFilterActivity);
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_filter)

        strToken=SharedPrefManager.getInstance(this).user.strToken!!
        if(Utilities.checkInternetConnection(this@OrderFilterActivity)){
            presenter.distributerList(strToken)
            presenter.employeeList(strToken)
        }else{
            AlertHelper.showNoInternetSnackBar(this@OrderFilterActivity,object :AlertHelper.SnackBarListener{
                override fun onOkClick() {
                    presenter.distributerList(strToken)
                    presenter.employeeList(strToken)
                }

            })
        }

        nav_back.setOnClickListener {
            finish()

        }

        val bundle: Bundle? = intent.extras
        if(intent.extras!=null){
            arrSelectedStatusWise = intent.getStringArrayListExtra("arrFilteredStatusOrder")
            arrSelExeIds = intent.getStringArrayListExtra("arrFilteredExeOrder")
            arrSelDistIds = intent.getStringArrayListExtra("arrFilteredDistOrder")
            fromDate=intent.getStringExtra("fromDate")
            toDate=intent.getStringExtra("toDate")

        }

        btApply.setOnClickListener {
            fromDate=tvOrdFromDate.text.toString().trim()
            toDate=tvOrdToDate.text.toString().trim()
            intent= Intent(applicationContext, OrderListActivity::class.java)
            intent.putExtra("arrSelStatus",arrSelectedStatusWise as ArrayList<String>)
            intent.putExtra("arrSelExeIds",arrSelExeIds as ArrayList<String>)
            intent.putExtra("arrSelDistIds",arrSelDistIds as ArrayList<String>)
            intent.putExtra("fromDate",fromDate)
            intent.putExtra("toDate",toDate)
            startActivity(intent)
        }
        btCancel.setOnClickListener {
            finish()
        }
        tvOrdFromDate.text=fromDate
        tvOrdToDate.text=toDate

        lvFilterOrderMainCategory.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                if (position == 0) {
                    lvFilterOrderDetailCategory.visibility=View.VISIBLE
                    datePickerLayout.visibility=View.GONE
                    val statusWiseOrder = arrayListOf("PENDING", "CONFIRM", "SHIPPED","DELIVERED","CANCEL","RETURNED","REFUNDED")
                    val adapter = FilterCategoryAdapter(this,statusWiseOrder,this,arrSelectedStatusWise)
                    lvFilterOrderDetailCategory.adapter=adapter;
                }

                if(position==1){
                    lvFilterOrderDetailCategory.visibility=View.VISIBLE
                    datePickerLayout.visibility=View.GONE
                    val adapter = FilterOrderDistributerAdapter(this,arrDistributorName,arrDistributorIds,this,arrSelDistIds)
                    lvFilterOrderDetailCategory.adapter=adapter;

                }

                if(position==2){
                    lvFilterOrderDetailCategory.visibility=View.VISIBLE
                    datePickerLayout.visibility=View.GONE
                    val adapter = FilterOrderExecutiveAdapter(this,arrExecutiveName,arrExecutiveIds,this,arrSelExeIds)
                    lvFilterOrderDetailCategory.adapter=adapter;

                }
                if (position == 3) {
                    lvFilterOrderDetailCategory.visibility=View.GONE
                    datePickerLayout.visibility=View.VISIBLE
                }
            }




        val fromDateCalendar = Calendar.getInstance()
        val fromDatePickerListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                fromDateCalendar.set(Calendar.YEAR, year)
                fromDateCalendar.set(Calendar.MONTH, monthOfYear)
                fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                fromDate =
                    fromDateCalendar.get(Calendar.YEAR).toString() + "-" + (fromDateCalendar.get(
                        Calendar.MONTH
                    ) + 1) + "-" + fromDateCalendar.get(Calendar.DAY_OF_MONTH)
                //etFromDate.setText(fromDate);
                toDatePicker.datePicker.minDate = fromDateCalendar.timeInMillis
                tvOrdFromDate.text = fromDate
            }

        val toDateCalendar = Calendar.getInstance()
        val toDatePickerListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                toDateCalendar.set(Calendar.YEAR, year)
                toDateCalendar.set(Calendar.MONTH, monthOfYear)
                toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                toDate = toDateCalendar.get(Calendar.YEAR).toString() + "-" + (toDateCalendar.get(
                    Calendar.MONTH
                ) + 1) + "-" + toDateCalendar.get(Calendar.DAY_OF_MONTH)
                //etToDate.setText(toDate);

                tvOrdToDate.text = toDate
            }

        toDatePicker = DatePickerDialog(
            this,
            toDatePickerListener,
            toDateCalendar.get(Calendar.YEAR),
            toDateCalendar.get(Calendar.MONTH),
            toDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
        toDatePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis

        fromDatePicker = DatePickerDialog(
            this,
            fromDatePickerListener,
            fromDateCalendar.get(Calendar.YEAR),
            fromDateCalendar.get(Calendar.MONTH),
            fromDateCalendar.get(Calendar.DAY_OF_MONTH)
        )
        fromDatePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis


        tvOrdFromDate.setOnClickListener {

            fromDatePicker.show()

        }



        tvOrdToDate.setOnClickListener {

            toDatePicker.show()
        }

        tvClearDate.setOnClickListener {
           tvOrdToDate.text=""
            tvOrdFromDate.text=""

        }


    }

    override fun onDistributerListSuccess(apiResponse: DistributerListResponse) {
      //  Toast.makeText(this,"Distributor Success",Toast.LENGTH_SHORT).show()
        arrDistributorDetail=apiResponse.arrList as ArrayList<ArrDistributerList>
        for(i in arrDistributorDetail){
            arrDistributorName.add(i.strName)
            arrDistributorIds.add(i._id)
        }
    }

    override fun onDistributerListNull(apiResponse: DistributerListResponse) {

    }

    override fun onDistributerListFailed(apiResponse: ResponseBody) {
        Toast.makeText(this,"Distributor Failed",Toast.LENGTH_SHORT).show()
    }

    override fun onDistributerListFailedServerError(string: String) {
        Toast.makeText(this,"Distributor Server Error",Toast.LENGTH_SHORT).show()
    }

    override fun onEmployeeListSuccess(apiResponse: EmployeeListResponse) {

        arrExecutiveDetail=apiResponse.arrList as ArrayList<ArrEmployeeList>
        for(i in arrExecutiveDetail){
            arrExecutiveName.add(i.strName)
            arrExecutiveIds.add(i._id)
        }
     //   Toast.makeText(this,"Employee Success",Toast.LENGTH_SHORT).show()
    }

    override fun onEmployeeListNull(apiResponse: EmployeeListResponse) {
    }

    override fun onEmployeeListFailed(apiResponse: ResponseBody) {
        Toast.makeText(this,"Employee Failed",Toast.LENGTH_SHORT).show()
    }

    override fun onEmployeeListFailedServerError(string: String) {
        Toast.makeText(this,"Employee Server Failed",Toast.LENGTH_SHORT).show()
    }

    override fun setValues(al: ArrayList<String>) {
        arrSelectedStatusWise=al
    }

    override fun setExeValue(arrSelectedExecutiveIds: ArrayList<String>) {
        arrSelExeIds=arrSelectedExecutiveIds
    }

    override fun setDistValue(arrSelectedDistributorIds: ArrayList<String>) {
        arrSelDistIds=arrSelectedDistributorIds
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}