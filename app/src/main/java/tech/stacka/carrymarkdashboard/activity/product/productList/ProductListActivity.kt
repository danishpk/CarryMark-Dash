package tech.stacka.carrymarkdashboard.activity.product.productList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.product.filterProduct.ProductFilterActivity
import tech.stacka.carrymarkdashboard.adapter.ProductListAdapter
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.ProductListResponse
import tech.stacka.carrymarkdashboard.models.data.ArrProductList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager

class ProductListActivity : AppCompatActivity(),ProductView,
    ProductListAdapter.DataTransferInterfacePr {
    var strToken:String = ""
    private var mTotalItemCount = 0
    private var mLastVisibleItemPosition: Int = 0
    private val mPostsPerPage = 10L
    private var isAvailable = true
    private var isLoading = false
    var itemUnitType="strName"
    var order="ASC"
    val presenter = ProductPresenter(this, this)
    private lateinit var mAdapter: ProductListAdapter
    private var productList = ArrayList<ArrProductList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        nav_back.setOnClickListener {
            finish()
        }

        presenter.productList(itemUnitType,order)
        tvSort.setOnClickListener {
            var sortBottomSheetDialog  =   BottomSheetDialog(this)
            val   layoutButtomSheetView  = this.layoutInflater.inflate(R.layout.ly_sort_buttom_sheet_frg_dialog, null)
            val radios=layoutButtomSheetView.findViewById<RadioGroup>(R.id.radios)
            val radioButton1=layoutButtomSheetView.findViewById<RadioButton>(R.id.radioSortAscending)
            val radioButton2=layoutButtomSheetView.findViewById<RadioButton>(R.id.radioSortDescending)
            val radioButton3=layoutButtomSheetView.findViewById<RadioButton>(R.id.radioSortDateAscending)
            val radioButton4=layoutButtomSheetView.findViewById<RadioButton>(R.id.radioSortDateDescending)
            Log.e("Radio Tag",itemUnitType)
            Log.e("Order Tag",order)
//            if(radioButton1.isChecked){
            radios.setOnCheckedChangeListener{ radioGroup: RadioGroup, checkedId ->

                when(checkedId){

                    R.id.radioSortAscending->{
                        radioButton1.isChecked = true
                        itemUnitType = "strName"
                        order="ASC"
                        presenter.productList(itemUnitType,order)
                        sortBottomSheetDialog.dismiss()
                    }

                    R.id.radioSortDescending->{
                        radioButton2.isChecked = true
                        itemUnitType = "strName"
                        order="DSC"
                        presenter.productList(itemUnitType,order)
                        sortBottomSheetDialog.dismiss()
                    }

                    R.id.radioSortDateAscending->{
                        radioButton3.isChecked = true
                        itemUnitType = "strDate"
                        order="ASC"
                        presenter.productList(itemUnitType,order)
                        sortBottomSheetDialog.dismiss()
                    }

                    R.id.radioSortDateDescending->{
                        radioButton4.isChecked = true
                        itemUnitType = "strDate"
                        order="DSC"
                        presenter.productList(itemUnitType,order)
                        sortBottomSheetDialog.dismiss()
                    }
                }
            }
            sortBottomSheetDialog.setContentView(layoutButtomSheetView)
            sortBottomSheetDialog.show()





        }
        tvFilterlist.setOnClickListener {
            startActivity(Intent(this, ProductFilterActivity::class.java))
        }

       var filterCategory=intent.getStringArrayExtra("filterCategory")
        if(filterCategory!=null){

        }

    }

    override fun onProductListSuccess(apiResponse: ProductListResponse) {
        val strLastItemCount:String=apiResponse.intTotalCount.toString();
        productList= apiResponse.arrList as ArrayList<ArrProductList>
        val mLayoutManager = LinearLayoutManager(this)
        rvProductList.layoutManager = mLayoutManager
        rvProductList.setHasFixedSize(true)
        mAdapter = ProductListAdapter(applicationContext,productList,strLastItemCount,this)
    //    mAdapter.addAll(productList)
        rvProductList.adapter = mAdapter
                            pbLoadMore.visibility = View.GONE
                    pbLoading.visibility = View.GONE
                    isLoading = false
    }

    override fun onProductListFailed(apiResponse: ResponseBody) {
       Toast.makeText(applicationContext,"response failed",Toast.LENGTH_SHORT).show()
    }

    override fun onProductListFailedServerError(string: String) {
        Toast.makeText(applicationContext,"connection failed",Toast.LENGTH_SHORT).show()
    }

    override fun onProductListNull(apiResponse: ProductListResponse) {
        TODO("Not yet implemented")
    }

    override fun onDeleteProductSuccess(apiResponse: DefaultResponse) {
        Toast.makeText(applicationContext,apiResponse.strMessage,Toast.LENGTH_SHORT).show()
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    override fun onDeleteProductNull(apiResponse: DefaultResponse) {
        TODO("Not yet implemented")
    }

    override fun onDeleteProductFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,apiResponse.toString(),Toast.LENGTH_LONG).show()
    }

    override fun onDeleteProductFailedServerError(toString: String) {
        TODO("Not yet implemented")
    }

    override fun setValues(strProductId: String) {
        presenter.deleteProduct(strToken,strProductId)
    }
}