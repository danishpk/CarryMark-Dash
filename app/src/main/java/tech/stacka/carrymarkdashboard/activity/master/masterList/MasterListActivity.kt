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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_master.*
import kotlinx.android.synthetic.main.activity_master_list.*
import kotlinx.android.synthetic.main.activity_master_list.sp_master_categorry
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.master_list_item.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.adapter.MasterAdapter
import tech.stacka.carrymarkdashboard.adapter.ProductListAdapter
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.GetMasterResponse
import tech.stacka.carrymarkdashboard.models.data.*
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.Utilities

class MasterListActivity : AppCompatActivity(), MasterListView ,MasterAdapter.DataTransferInterfaceMaster{
    var MASTER_SUCCESS:Int=0
    var pos=0
    var arrClnBrand= ArrayList<ClnCategories>()
    var arrClnCategory= ArrayList<ClnCategories>()
    var arrClnMaterial= ArrayList<ClnCategories>()
    var arrClnSize= ArrayList<ClnCategories>()
    var arrClnColor= ArrayList<ClnCategories>()
    var arrClnLocation= ArrayList<ClnCategories>()
    var arrClnSubCategory= ArrayList<ClnCategories>()
    var strToken:String=""
    var strCategoryType:String=""
    val presenter=MasterListPresenter(this,this)
    val categoryList= ArrayList<String>()
    val mLayoutManager = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_list)

        nav_back.setOnClickListener {
            finish()
        }
        categoryList.add("cln_brand")
        categoryList.add("cln_category")
        categoryList.add("cln_material")
        categoryList.add("cln_size")
        categoryList.add("cln_color")
        categoryList.add("cln_sub_category")

        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
//        val adapter= ArrayAdapter<String>(applicationContext,R.layout.support_simple_spinner_dropdown_item,categoryList)
//        sp_master_categorry.adapter=adapter;

        presenter.getMaster(categoryList,30)

        val mLayoutManager = LinearLayoutManager(this)
                sp_master_categorry.onItemSelectedListener = object: AdapterView.OnItemSelectedListener,
                    MasterAdapter.DataTransferInterfaceMaster {
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                //text_view.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
                if(position==1){
                    pos=position;
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnBrand,this)
                    rvMasterList.adapter = mAdapter
                    strCategoryType= categoryList[position-1].toString()
                }
                if(position==2){
                    pos=position;
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnCategory,this)
                    rvMasterList.adapter = mAdapter
                    strCategoryType= categoryList[position-1].toString()
                }

                if(position==3){
                    pos=position;
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnMaterial,this)
                    rvMasterList.adapter = mAdapter
                    strCategoryType= categoryList[position-1].toString()
                }

                if(position==4){
                    pos=position;
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnSize,this)
                    rvMasterList.adapter = mAdapter
                    strCategoryType= categoryList[position-1].toString()
                }

                if(position==5){
                    pos=position;
                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnColor,this)
                    rvMasterList.adapter = mAdapter
                    strCategoryType= categoryList[position-1].toString()
                }

                if(position==6){
                    pos=position;
//                    rvMasterList.layoutManager = mLayoutManager
//                    rvMasterList.setHasFixedSize(true)
//                    val mAdapter = MasterAdapter(applicationContext,arrClnLocation,this)
//                    rvMasterList.adapter = mAdapter
//                    strCategoryType=categoryList.get(position-1).toString()

                    rvMasterList.layoutManager = mLayoutManager
                    rvMasterList.setHasFixedSize(true)
                    val mAdapter = MasterAdapter(applicationContext,arrClnSubCategory,this)
                    rvMasterList.adapter = mAdapter
                    strCategoryType= categoryList[position-1].toString()
                }
//                if(position==7){
//
//                }
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }

                    override fun setValues(strMasterId: String) {
                        presenter.deleteMaster(strCategoryType,strMasterId,strToken)
                    }
                }





    }


    override fun onResume() {
        super.onResume()
    }

    override fun onMasterListSuccess(apiResponse: GetMasterResponse) {
        //Toast.makeText(applicationContext,"Success",Toast.LENGTH_LONG).show()
        val strMainCategory=sp_master_categorry.selectedItem.toString().trim()
         arrClnBrand= apiResponse.cln_brand as ArrayList<ClnCategories>
        arrClnCategory= apiResponse.cln_category as ArrayList<ClnCategories>
         arrClnMaterial= apiResponse.cln_material as ArrayList<ClnCategories>
         arrClnSize= apiResponse.cln_size as ArrayList<ClnCategories>
         arrClnColor= apiResponse.cln_color as ArrayList<ClnCategories>
       //  arrClnLocation= apiResponse.cln_location as ArrayList<ClnCategories>
        arrClnSubCategory= apiResponse.cln_sub_category as ArrayList<ClnCategories>

    }

    override fun onMasterListNull(apiResponse: GetMasterResponse) {
    }

    override fun onMasterListFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,"Failed",Toast.LENGTH_LONG).show()
    }

    override fun onMasterServerFailed(toString: String) {
        Toast.makeText(applicationContext,toString,Toast.LENGTH_LONG).show()
    }

    override fun onDelMasterSuccess(apiResponse: DefaultResponse) {
        Toast.makeText(applicationContext,apiResponse.strMessage,Toast.LENGTH_SHORT).show()
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);

//        sp_master_categorry.setSelection(pos)
//        if(pos==1){
//            rvMasterList.layoutManager = mLayoutManager
//            rvMasterList.setHasFixedSize(true)
//            val mAdapter = MasterAdapter(applicationContext,arrClnBrand,this)
//            rvMasterList.adapter = mAdapter
//            strCategoryType= categoryList[pos-1].toString()
//        }
//        if(pos==2){
//            rvMasterList.layoutManager = mLayoutManager
//            rvMasterList.setHasFixedSize(true)
//            val mAdapter = MasterAdapter(applicationContext,arrClnCategory,this)
//            rvMasterList.adapter = mAdapter
//            strCategoryType= categoryList[pos-1].toString()
//        }
//
//        if(pos==3){
//            rvMasterList.layoutManager = mLayoutManager
//            rvMasterList.setHasFixedSize(true)
//            val mAdapter = MasterAdapter(applicationContext,arrClnMaterial,this)
//            rvMasterList.adapter = mAdapter
//            strCategoryType= categoryList[pos-1].toString()
//        }
//
//        if(pos==4){
//            rvMasterList.layoutManager = mLayoutManager
//            rvMasterList.setHasFixedSize(true)
//            val mAdapter = MasterAdapter(applicationContext,arrClnSize,this)
//            rvMasterList.adapter = mAdapter
//            strCategoryType= categoryList[pos-1].toString()
//        }
//
//        if(pos==5){
//
//            rvMasterList.layoutManager = mLayoutManager
//            rvMasterList.setHasFixedSize(true)
//            val mAdapter = MasterAdapter(applicationContext,arrClnColor,this)
//            rvMasterList.adapter = mAdapter
//            strCategoryType= categoryList[pos-1].toString()
//        }
//
//        if(pos==6){
////                    rvMasterList.layoutManager = mLayoutManager
////                    rvMasterList.setHasFixedSize(true)
////                    val mAdapter = MasterAdapter(applicationContext,arrClnLocation,this)
////                    rvMasterList.adapter = mAdapter
////                    strCategoryType=categoryList.get(position-1).toString()
//
//            rvMasterList.layoutManager = mLayoutManager
//            rvMasterList.setHasFixedSize(true)
//            val mAdapter = MasterAdapter(applicationContext,arrClnSubCategory,this)
//            rvMasterList.adapter = mAdapter
//            strCategoryType= categoryList[pos-1].toString()
//        }
    }

    override fun onDelMasterNull(apiResponse: DefaultResponse) {
        Toast.makeText(applicationContext,apiResponse.strMessage,Toast.LENGTH_SHORT).show()    }

    override fun onDelMasterFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,apiResponse.toString(),Toast.LENGTH_SHORT).show()    }

    override fun onDelMasterServerFailed(toString: String) {
        Toast.makeText(applicationContext,toString,Toast.LENGTH_SHORT).show()
    }

    override fun setValues(strMasterId: String) {

            presenter.deleteMaster(strCategoryType,strMasterId,strToken)

    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}