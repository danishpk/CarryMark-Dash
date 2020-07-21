package tech.stacka.carrymarkdashboard.activity.product.filterProduct

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.product.productList.ProductListActivity
import tech.stacka.carrymarkdashboard.adapter.FilterCategoryAdapter
import tech.stacka.carrymarkdashboard.adapter.FilterCategoryBrandAdapter
import tech.stacka.carrymarkdashboard.adapter.FilterCategoryMaterialAdapter
import tech.stacka.carrymarkdashboard.models.FilterCategoryResponse

class ProductFilterActivity : AppCompatActivity(), ProductFilterView,
    FilterCategoryAdapter.DataTransferInterface,
    FilterCategoryBrandAdapter.DataTransferInterfaceBrand,
    FilterCategoryMaterialAdapter.DataMaterialTransferInterface {
    private var filterCategoryBrand = ArrayList<String>()
    private var filterCategoryItem=ArrayList<String>()
    private var filterCategoryMaterial=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        var categoryList= ArrayList<String>()
        categoryList.add("cln_brand")
        categoryList.add("cln_category")
        categoryList.add("cln_material")
        tv_Nav.text="Filters"
        val presenter = ProductFilterPresenter(this, this)
        presenter.filtercategoryList(categoryList,10)
        nav_back.setOnClickListener {
            finish()

        }

        val bundle: Bundle? = intent.extras
        if(intent.extras!=null){
            filterCategoryBrand = intent.getStringArrayListExtra("filterCategoryBrand")
            filterCategoryItem = intent.getStringArrayListExtra("filterCategoryItem")
            filterCategoryMaterial = intent.getStringArrayListExtra("filterCategoryMaterial")
        }
        btApply.setOnClickListener {
         intent= Intent(applicationContext, ProductListActivity::class.java)
            intent.putExtra("filterCategoryBrand",filterCategoryBrand as ArrayList<String>)
            intent.putExtra("filterCategoryItem",filterCategoryItem as ArrayList<String>)
            intent.putExtra("filterCategoryMaterial",filterCategoryMaterial as ArrayList<String>)
//            intent.putStringArrayListExtra("categoryFilter",
//                filterCategory as java.util.ArrayList<String>?)
            startActivity(intent)
            finish()

        }
        btCancel.setOnClickListener {
            finish()
        }

    }

    override fun onFilterCategoryListSuccess(apiResponse: FilterCategoryResponse) {
        val categorybrandList= ArrayList<String>()
        val categorycatList= ArrayList<String>()
        val categorymaterialList= ArrayList<String>()
        var i: Int = 0
        for (st in apiResponse.cln_brand) {
            categorybrandList.add(apiResponse.cln_brand[i].strName)
            i++
        }
        var k: Int = 0
        for (_id in apiResponse.cln_category) {
            categorycatList.add(apiResponse.cln_category[k].strName)
            k++
        }
        var j: Int = 0
        for (data in apiResponse.cln_material) {
            categorymaterialList.add(apiResponse.cln_material[j].strName)
            j++
        }

        lvFilterMainCategory.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                    if (position == 0) {
                        view.setBackgroundColor(Color.LTGRAY)
//                        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categorybrandList)
//                        lvFilterDetails.adapter=arrayAdapter
                        val adapter =FilterCategoryBrandAdapter(this,categorybrandList,this,filterCategoryBrand)
                        lvFilterDetails.adapter=adapter
                    }
                    if (position == 1) {
//                        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categorycatList)
//                        lvFilterDetails.adapter=arrayAdapter
                        val adapter =FilterCategoryAdapter(this,categorycatList,this,filterCategoryItem)
                        lvFilterDetails.adapter=adapter

                    }

                    if (position == 2) {
//                        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categorycatList)
//                        lvFilterDetails.adapter=arrayAdapter
                        val adapter =FilterCategoryMaterialAdapter(this,categorymaterialList,this,filterCategoryMaterial)
                        lvFilterDetails.adapter=adapter

                    }

                }
    }

    override fun onFilterProductCategoryListFailed(apiResponse: ResponseBody) {
    }

    override fun onFilterProductCategoryServerFailed(string: String) {
    }

    override fun onFilterProductCategoryListNull(apiResponse: FilterCategoryResponse) {
    }

    override fun setValues(al: ArrayList<String>) {
        filterCategoryItem=al

    }

    override fun setValueData(data:ArrayList<String>) {
        filterCategoryBrand=data
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun setMaterialValues(materialList: ArrayList<String>) {
        filterCategoryMaterial=materialList
    }
}
