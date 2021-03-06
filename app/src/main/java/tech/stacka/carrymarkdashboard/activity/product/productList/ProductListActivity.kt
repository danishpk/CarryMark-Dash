package tech.stacka.carrymarkdashboard.activity.product.productList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import tech.stacka.carrymarkdashboard.utils.Utilities

class ProductListActivity : AppCompatActivity(),ProductView,
    ProductListAdapter.DataTransferInterfacePr {

    var strToken:String = ""
    var arrFilteredBrand=ArrayList<String>()
    var arrFilteredCategory=ArrayList<String>()
    var arrFilteredMaterial=ArrayList<String>()
    private var mTotalItemCount = 0
    private var currentPage=0;
    private var offset=0;
    private var totalPage=0;
    private var limit=20;
    private var seachLimit=30;
    private var isLoading = false
    var itemUnitType="strName"
    var order="ASC"
    var sortBy="ascName"
    var strSearchKey:String=""
    var SEARCH_VIS_FLAG=false
    var SEARCHED_FLAG=false
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
        searchButton.visibility=View.VISIBLE
        searchButton.setOnClickListener {
            if(SEARCH_VIS_FLAG){
                searchRow.visibility=View.GONE
                SEARCH_VIS_FLAG=false

                if(SEARCHED_FLAG){
                    pbLoading.visibility = View.VISIBLE
                    if (Utilities.checkInternetConnection(this)) {
                        presenter.productList(
                            itemUnitType,
                            order,
                            arrFilteredBrand,
                            arrFilteredCategory,
                            arrFilteredMaterial,
                            offset,
                            limit
                        )
                    } else {
                        pbLoadMore.visibility = View.GONE
                        pbLoading.visibility = View.GONE
                        isLoading = false
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
                presenter.searchList(itemUnitType,order,arrFilteredBrand,arrFilteredCategory,arrFilteredMaterial,offset,limit,strSearchKey)
            }else{
                pbLoadMore.visibility = View.GONE
                pbLoading.visibility = View.GONE
                isLoading = false
                Toast.makeText(this,"check your internet connection", Toast.LENGTH_LONG).show()
            }
        }

        val bundle: Bundle? = intent.extras
        if(intent.extras!=null){
            arrFilteredBrand = intent.getStringArrayListExtra("filterCategoryBrand")
            arrFilteredCategory = intent.getStringArrayListExtra("filterCategoryItem")
            arrFilteredMaterial = intent.getStringArrayListExtra("filterCategoryMaterial")
        }
        if(Utilities.checkInternetConnection(this)) {
            presenter.productList(itemUnitType,order,arrFilteredBrand,arrFilteredCategory,arrFilteredMaterial,offset,limit)
        }else{
            pbLoadMore.visibility = View.GONE
            pbLoading.visibility = View.GONE
            isLoading = false
            Toast.makeText(this,"check your internet connection", Toast.LENGTH_LONG).show()
        }

        btLoadMore.setOnClickListener {
        pbLoadMore.visibility=View.VISIBLE
            btLoadMore.visibility=View.GONE
            currentPage++
            if (currentPage <= totalPage) {
                val offsetNw: Int = (currentPage)
                presenter.performPagination(itemUnitType,order,arrFilteredBrand,arrFilteredCategory,arrFilteredMaterial,offsetNw,limit)
            } else {
                pbLoadMore.visibility=View.GONE
                btLoadMore.visibility=View.GONE
                Toast.makeText(this@ProductListActivity, "No more products", Toast.LENGTH_SHORT).show()
            }
        }


        tvSort.setOnClickListener {
            var sortBottomSheetDialog  =   BottomSheetDialog(this)
            val   layoutButtomSheetView  = this.layoutInflater.inflate(R.layout.ly_sort_buttom_sheet_frg_dialog, null)
            val radios=layoutButtomSheetView.findViewById<RadioGroup>(R.id.radios)
            val radioButton1=layoutButtomSheetView.findViewById<RadioButton>(R.id.radioSortAscending)
            val radioButton2=layoutButtomSheetView.findViewById<RadioButton>(R.id.radioSortDescending)
            val radioButton3=layoutButtomSheetView.findViewById<RadioButton>(R.id.radioSortPriceAscending)
            val radioButton4=layoutButtomSheetView.findViewById<RadioButton>(R.id.radioSortPriceDescending)
            if(sortBy=="ascName"){
                radioButton1.isChecked=true
            }else if(sortBy=="dscName"){
                radioButton2.isChecked=true
            }else if(sortBy=="ascPrice"){
                radioButton3.isChecked=true
            }else if(sortBy=="dscPrice"){
                radioButton4.isChecked=true
            }
            radios.setOnCheckedChangeListener{ radioGroup: RadioGroup, checkedId ->

                when(checkedId){
                    R.id.radioSortAscending->{
                        radioButton1.isChecked = true
                        itemUnitType = "strName"
                        order="ASC"
                        sortBy="ascName"
                        presenter.productList(itemUnitType,order,arrFilteredBrand,arrFilteredCategory,arrFilteredMaterial,offset,limit)
                        sortBottomSheetDialog.dismiss()
                    }

                    R.id.radioSortDescending->{
                        radioButton2.isChecked = true
                        itemUnitType = "strName"
                        order="DSC"
                        sortBy="dscName"
                        presenter.productList(itemUnitType,order,arrFilteredBrand,arrFilteredCategory,arrFilteredMaterial,offset,limit)
                        sortBottomSheetDialog.dismiss()
                    }

                    R.id.radioSortPriceAscending->{
                        radioButton3.isChecked = true
                        itemUnitType = "dblMRP"
                        order="ASC"
                        sortBy="ascPrice"
                        presenter.productList(itemUnitType,order,arrFilteredBrand,arrFilteredCategory,arrFilteredMaterial,offset,limit)
                        sortBottomSheetDialog.dismiss()
                    }

                    R.id.radioSortPriceDescending->{
                        radioButton4.isChecked = true
                        itemUnitType = "dblMRP"
                        order="DSC"
                        sortBy="dscPrice"
                        presenter.productList(itemUnitType,order,arrFilteredBrand,arrFilteredCategory,arrFilteredMaterial,offset,limit)
                        sortBottomSheetDialog.dismiss()
                    }
                }
            }
            sortBottomSheetDialog.setContentView(layoutButtomSheetView)
            sortBottomSheetDialog.show()

        }
        tvFilterlist.setOnClickListener {
            intent= Intent(applicationContext, ProductFilterActivity::class.java)
            intent.putExtra("filterCategoryBrand",arrFilteredBrand as ArrayList<String>)
            intent.putExtra("filterCategoryItem",arrFilteredCategory as ArrayList<String>)
            intent.putExtra("filterCategoryMaterial",arrFilteredMaterial as ArrayList<String>)
            startActivity(intent)
        }


    }

    override fun onProductListSuccess(apiResponse: ProductListResponse) {
        noDataLayoutSubject.visibility = View.GONE
        rvProductList.visibility=View.VISIBLE
        if(apiResponse.intTotalCount>limit){
            btLoadMore.visibility=View.VISIBLE
        }
        if(productList!=null) {
            mTotalItemCount = apiResponse.intTotalCount
            totalPage = mTotalItemCount / limit
            productList = apiResponse.arrList as ArrayList<ArrProductList>


            if (apiResponse.arrList.isEmpty()) {
                pbLoadMore.visibility = View.GONE
                pbLoading.visibility = View.GONE
                btLoadMore.visibility = View.GONE
                noDataLayoutSubject.visibility = View.VISIBLE
                rvProductList.visibility=View.GONE
            }else {
                Log.e("Images", productList[0].strImageUrl)
                val mLayoutManager = LinearLayoutManager(this)
                rvProductList.layoutManager = mLayoutManager
                rvProductList.setHasFixedSize(true)
                mAdapter =
                    ProductListAdapter(applicationContext, productList, mTotalItemCount, this)
                rvProductList.adapter = mAdapter
                pbLoadMore.visibility = View.GONE
                pbLoading.visibility = View.GONE
                isLoading = false
            }
        }
    }

    override fun onProductListFailed(apiResponse: ResponseBody) {
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        isLoading = false
       Toast.makeText(applicationContext,"response failed",Toast.LENGTH_SHORT).show()
    }

    override fun onProductListFailedServerError(string: String) {
        pbLoadMore.visibility = View.GONE
        pbLoading.visibility = View.GONE
        isLoading = false
        Toast.makeText(applicationContext,"connection failed",Toast.LENGTH_SHORT).show()
    }

    override fun onProductListNull(apiResponse: ProductListResponse) {
    }

    override fun onDeleteProductSuccess(apiResponse: DefaultResponse) {
        Toast.makeText(applicationContext,apiResponse.strMessage,Toast.LENGTH_SHORT).show()
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    override fun onDeleteProductNull(apiResponse: DefaultResponse) {
    }

    override fun onDeleteProductFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,apiResponse.toString(),Toast.LENGTH_LONG).show()
    }

    override fun onDeleteProductFailedServerError(toString: String) {
        Toast.makeText(applicationContext,toString,Toast.LENGTH_LONG).show()
    }

    override fun onProductListMoreSuccess(apiResponse: ProductListResponse) {
        productList= apiResponse.arrList as ArrayList<ArrProductList>
        mAdapter.addAll(productList)
        pbLoadMore.visibility=View.GONE
        btLoadMore.visibility = View.VISIBLE
        if(currentPage==totalPage){
            btLoadMore.visibility=View.GONE
            tvNoMore.visibility=View.VISIBLE
        }
    }

    override fun onProductListMoreNull(apiResponse: ProductListResponse) {
        pbLoadMore.visibility=View.GONE
        btLoadMore.visibility = View.VISIBLE
    }

    override fun onProductListMoreFailed(apiResponse: ResponseBody) {
        pbLoadMore.visibility=View.GONE
        btLoadMore.visibility = View.VISIBLE
    }

    override fun onProductListFailedMoreServerError(string: String) {
        pbLoadMore.visibility=View.GONE
        btLoadMore.visibility = View.VISIBLE
    }

    override fun onProductSearchListSuccess(apiResponse: ProductListResponse) {
        SEARCHED_FLAG=true
        rvProductList.visibility=View.VISIBLE
        noDataLayoutSubject.visibility=View.GONE
        btLoadMore.visibility=View.GONE
        productList= apiResponse.arrList as ArrayList<ArrProductList>
        val mLayoutManager = LinearLayoutManager(this)
        rvProductList.layoutManager = mLayoutManager
        rvProductList.setHasFixedSize(true)
        if(apiResponse.arrList.isEmpty()){
            pbLoadMore.visibility = View.GONE
            pbLoading.visibility = View.GONE
            btLoadMore.visibility = View.GONE
            noDataLayoutSubject.visibility=View.VISIBLE
            rvProductList.visibility=View.GONE
        }else {
            mAdapter = ProductListAdapter(applicationContext, productList, mTotalItemCount, this)
            rvProductList.adapter = mAdapter
            pbLoadMore.visibility = View.GONE
            pbLoading.visibility = View.GONE
            isLoading = false
        }
    }

    override fun onProductSearchListNull(apiResponse: ProductListResponse) {
        SEARCHED_FLAG=true
        pbLoadMore.visibility=View.GONE
        btLoadMore.visibility = View.VISIBLE
    }

    override fun onProductSearchListFailed(apiResponse: ResponseBody) {
        SEARCHED_FLAG=true
        pbLoadMore.visibility=View.GONE
        btLoadMore.visibility = View.VISIBLE
    }

    override fun onProductListSearchFailedServerError(string: String) {
        SEARCHED_FLAG=true
        pbLoadMore.visibility=View.GONE
        btLoadMore.visibility = View.VISIBLE
    }

    override fun setValues(strProductId: String) {
        presenter.deleteProduct(strToken,strProductId)
    }

    override fun onBackPressed() {

        if(SEARCH_VIS_FLAG){
            searchRow.visibility=View.GONE
            SEARCH_VIS_FLAG=false
            if(SEARCHED_FLAG) {
                pbLoading.visibility = View.VISIBLE
                if (Utilities.checkInternetConnection(this)) {
                    presenter.productList(
                        itemUnitType,
                        order,
                        arrFilteredBrand,
                        arrFilteredCategory,
                        arrFilteredMaterial,
                        offset,
                        limit
                    )
                } else {
                    pbLoadMore.visibility = View.GONE
                    pbLoading.visibility = View.GONE
                    isLoading = false
                    Toast.makeText(this, "check your internet connection", Toast.LENGTH_LONG).show()
                }
                SEARCHED_FLAG=false
            }
        }else{
            super.onBackPressed()
            finish()
        }

    }



}