package tech.stacka.carrymarkdashboard.activity.distributer.addDistributer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_distributer.*
import kotlinx.android.synthetic.main.activity_add_employee.etEmail
import kotlinx.android.synthetic.main.activity_add_employee.etMobile
import kotlinx.android.synthetic.main.activity_add_employee.etName
import kotlinx.android.synthetic.main.activity_add_employee.etPassword
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.ErrCommon
import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities

class AddDistributerActivity : AppCompatActivity(), AddDistributerView {
    var arrExecutiveIds=ArrayList<String>()
    var arrExecutiveName=ArrayList<String>()
    var arrExecutiveDetail=ArrayList<ArrEmployeeList>()
    var arrErrorCommon=ArrayList<ErrCommon>()
    var strToken:String = ""
    var strExecutiveName=""
    var strExecutiveId=""
    val presenter= AddDistributerPresenter(this,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_distributer)

        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        nav_back.setOnClickListener {
            finish()
        }

        if(Utilities.checkInternetConnection(this)) {
            presenter.employeeList(strToken)

        }else{
            AlertHelper.showNoInternetSnackBar(this@AddDistributerActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                    presenter.employeeList(strToken)
                }
            })
        }



//        sp_executiveD.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                }
//
//                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
//                    strExecutiveName=arrExecutiveName[pos]
//                    strExecutiveId=arrExecutiveIds[pos]
//                    tvExecutive.text=arrExecutiveName[pos]
//                }
//
//            }



    }




    fun btAddDistbtrClick(view: View) {
        val strName = etName.text.toString().trim()
        val strEmail = etEmail.text.toString().trim()
        val strPassword = etPassword.text.toString().trim()
        val strMobile = etMobile.text.toString().trim()
        val strAddress = etAddressDistr.text.toString().trim()

        if (strName.isEmpty()) {
            etName.error = "Employee name required*"
            etName.requestFocus()
            return
        }
        if (strMobile.isEmpty()) {
            etMobile.error = "Mobile number required*"
            etMobile.requestFocus()
            return
        }
        if (strEmail.isEmpty()) {
            etEmail.error = "Email id required*"
            etEmail.requestFocus()
            return
        }
        if (strPassword.isEmpty()) {
            etPassword.error = "Selling price required*"
            etPassword.requestFocus()
            return
        }

        if (strAddress.isEmpty()) {
            etAddressDistr.error = "Address required*"
            etAddressDistr.requestFocus()
            return
        }

        if(!Utilities.isValidPhoneNumber(strMobile)){
            etMobile.error = "Enter valid mobile number*"
            etMobile.requestFocus()
            return
        }
        if(!Utilities.emailValidator(strEmail)){
            etEmail.error = "Enter valid email*"
            etEmail.requestFocus()
            return
        }

        if(Utilities.checkInternetConnection(this)) {
            pbAddDist.visibility=View.VISIBLE
            btAddDist.visibility+View.GONE
            presenter.addDistributer(strToken,strName,strMobile,strEmail,strPassword,strAddress,strExecutiveId)
        }else{
            btAddDist.visibility+View.VISIBLE
            pbAddDist.visibility=View.GONE
            AlertHelper.showNoInternetSnackBar(this@AddDistributerActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                    pbAddDist.visibility=View.VISIBLE
                    btAddDist.visibility+View.GONE
                    presenter.addDistributer(strToken,strName,strMobile,strEmail,strPassword,strAddress,strExecutiveId)
                }
            })
        }

    }


    override fun onaddDistributerSuccess(apiResponse: DefaultResponse) {
        btAddDist.visibility+View.VISIBLE
        pbAddDist.visibility=View.GONE
        AlertHelper.showOKSnackBarAlert(this@AddDistributerActivity,"Distributor Added Successfully")
        etName.text.clear()
        etMobile.text.clear()
        etEmail.text.clear()
        etPassword.text.clear()
        etAddressDistr.text.clear()
    }

    override fun onaddDistributerNull(apiResponse: DefaultResponse) {
        btAddDist.visibility+View.VISIBLE
        pbAddDist.visibility=View.GONE
    }

    override fun onaddDistributerFailed(apiResponse: JSONArray) {
        Log.e("arrErrorComm", apiResponse.toString())
        val listData = ArrayList<String>()
        for (i in 0 until apiResponse.length()) {
            listData += apiResponse.getString(i)
            Log.e("ListData",listData.toString())
            for(i in listData){
                if(i=="USER NAME ALREADY EXIST"){
                    etName.error = "USER NAME ALREADY EXIST *"
                    etName.requestFocus()
                    btAddDist.visibility+View.VISIBLE
                    pbAddDist.visibility=View.GONE
                }else if(i=="MOBILE NUMBER ALREADY EXIST"){
                    etMobile.error = "MOBILE NUMBER ALREADY EXIST *"
                    etMobile.requestFocus()
                    btAddDist.visibility+View.VISIBLE
                    pbAddDist.visibility=View.GONE
                }
                else if(i=="EMAIL ALREADY EXIST"){
                    etEmail.error = "EMAIL ALREADY EXIST *"
                    etEmail.requestFocus()
                    btAddDist.visibility+View.VISIBLE
                    pbAddDist.visibility=View.GONE
                }
            }
        }
    }

    override fun onaddDistributerFailedServerError(string: String) {
        btAddDist.visibility+View.VISIBLE
        pbAddDist.visibility=View.GONE
        AlertHelper.showNoInternetSnackBar(this@AddDistributerActivity, object :
            AlertHelper.SnackBarListener {
            override fun onOkClick() {
            }
        })
    }

    override fun onEmployeeListSuccess(apiResponse: EmployeeListResponse) {
        arrExecutiveDetail=apiResponse.arrList as ArrayList<ArrEmployeeList>
        for(i in arrExecutiveDetail){
            arrExecutiveName.add(i.strName)
            arrExecutiveIds.add(i._id)
        }
        val spinner = findViewById<View>(R.id.spExecutive) as Spinner
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrExecutiveName)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                strExecutiveName=arrExecutiveName[position]
                strExecutiveId=arrExecutiveIds[position]
              //  tvExecutive.text=arrExecutiveName[position]
            }

        }
    }

    override fun onEmployeeListNull(apiResponse: EmployeeListResponse) {

    }

    override fun onEmployeeListFailed(apiResponse: ResponseBody) {

    }

    override fun onEmployeeListFailedServerError(string: String) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}