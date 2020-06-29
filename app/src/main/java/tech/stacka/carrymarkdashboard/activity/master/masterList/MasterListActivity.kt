package tech.stacka.carrymarkdashboard.activity.master.masterList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_master_list.*
import kotlinx.android.synthetic.main.activity_product_list.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.adapter.MasterAdapter
import tech.stacka.carrymarkdashboard.adapter.ProductListAdapter
import tech.stacka.carrymarkdashboard.models.GetMasterResponse
import tech.stacka.carrymarkdashboard.models.data.*

class MasterListActivity : AppCompatActivity(), MasterListView {
    var MASTER_SUCCESS:Int=0
    var arrClnBrand= ArrayList<ClnCategories>()
    var arrClnCategory= ArrayList<ClnCategories>()
    var arrClnMaterial= ArrayList<ClnCategories>()
    var arrClnSize= ArrayList<ClnCategories>()
    var arrClnColor= ArrayList<ClnCategories>()
    var arrClnLocation= ArrayList<ClnCategories>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_list)
        val categoryList= ArrayList<String>()
        categoryList.add("cln_brand")
        categoryList.add("cln_category")
        categoryList.add("cln_material")
        categoryList.add("cln_size")
        categoryList.add("cln_color")
        categoryList.add("cln_location")


//        val adapter= ArrayAdapter<String>(applicationContext,R.layout.support_simple_spinner_dropdown_item,categoryList)
//        sp_master_categorry.adapter=adapter;
        val presenter=MasterListPresenter(this,this)
        presenter.getMaster(categoryList,10)

        val mLayoutManager = LinearLayoutManager(this)
                sp_master_categorry.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                //text_view.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
                if(position==1){
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnBrand)
                    rvMasterList.adapter = mAdapter
                }
                if(position==2){
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnCategory)
                    rvMasterList.adapter = mAdapter
                }

                if(position==3){
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnMaterial)
                    rvMasterList.adapter = mAdapter
                }

                if(position==4){
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnSize)
                    rvMasterList.adapter = mAdapter
                }

                if(position==5){
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnColor)
                    rvMasterList.adapter = mAdapter
                }

                if(position==6){
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnLocation)
                    rvMasterList.adapter = mAdapter
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }






    }


    override fun onResume() {
        super.onResume()
    }

    override fun onMasterListSuccess(apiResponse: GetMasterResponse) {
        Toast.makeText(applicationContext,"Success",Toast.LENGTH_LONG).show()
        val strMainCategory=sp_master_categorry.selectedItem.toString().trim()
         arrClnBrand= apiResponse.cln_brand as ArrayList<ClnCategories>
        arrClnCategory= apiResponse.cln_category as ArrayList<ClnCategories>
         arrClnMaterial= apiResponse.cln_material as ArrayList<ClnCategories>
         arrClnSize= apiResponse.cln_size as ArrayList<ClnCategories>
         arrClnColor= apiResponse.cln_color as ArrayList<ClnCategories>
         arrClnLocation= apiResponse.cln_location as ArrayList<ClnCategories>

    }

    override fun onMasterListNull(apiResponse: GetMasterResponse) {
        TODO("Not yet implemented")
    }

    override fun onMasterListFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,"Failed",Toast.LENGTH_LONG).show()
    }

    override fun onMasterServerFailed(toString: String) {
        Toast.makeText(applicationContext,toString,Toast.LENGTH_LONG).show()
    }
}