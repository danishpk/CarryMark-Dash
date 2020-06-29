package tech.stacka.carrymarkdashboard.activity.product.filterProduct

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.product.productList.ProductListActivity
import tech.stacka.carrymarkdashboard.adapter.FilterCategoryAdapter
import tech.stacka.carrymarkdashboard.models.FilterCategoryResponse

class ProductFilterActivity : AppCompatActivity(), ProductFilterView,
    FilterCategoryAdapter.DataTransferInterface {
    var filterCategory: MutableList<String> = java.util.ArrayList()
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
        btApply.setOnClickListener {
         intent= Intent(applicationContext, ProductListActivity::class.java)
            intent.putExtra("filterCategory",filterCategory as ArrayList<String>)
//            intent.putStringArrayListExtra("categoryFilter",
//                filterCategory as java.util.ArrayList<String>?)
            startActivity(intent)

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
            categorybrandList.add(apiResponse.cln_brand.get(i).strName)
            i++
        }
        var k: Int = 0
        for (_id in apiResponse.cln_category) {
            categorycatList.add(apiResponse.cln_category.get(k).strName)
            k++
        }

        lvFilterMainCategory.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                    if (position == 0) {
//                        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categorybrandList)
//                        lvFilterDetails.adapter=arrayAdapter

                        val adapter =FilterCategoryAdapter(this,categorybrandList,this)
                        lvFilterDetails.adapter=adapter
                    }
                    if (position == 1) {
//                        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categorycatList)
//                        lvFilterDetails.adapter=arrayAdapter
                        val adapter =FilterCategoryAdapter(this,categorycatList,this)
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

    override fun setValues(al: MutableList<String>) {
        filterCategory=al
    }
}
