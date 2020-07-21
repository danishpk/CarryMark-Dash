package tech.stacka.carrymarkdashboard.activity.employee.addEmployee

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_employee.*
import kotlinx.android.synthetic.main.toolbar_child.*
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager
import tech.stacka.carrymarkdashboard.utils.AlertHelper
import tech.stacka.carrymarkdashboard.utils.Utilities
import java.util.regex.Matcher
import java.util.regex.Pattern

class AddEmployeeActivity : AppCompatActivity(), AddEmployeeView {
    var strToken:String = ""
    val presenter=AddEmployeePresenter(this,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)
        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        nav_back.setOnClickListener {
            finish()
        }
    }
    fun btAddEmplClick(view: View) {
        val strName = etName.text.toString().trim()
        val strEmail = etEmail.text.toString().trim()
        val strPassword = etPassword.text.toString().trim()
        val strMobile = etMobile.text.toString().trim()
        val strAddress = etAddress.text.toString().trim()

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
            etAddress.error = "Address required*"
            etAddress.requestFocus()
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
            pbAddEmpl.visibility=View.VISIBLE
            btAddEmpl.visibility+View.GONE
            presenter.addEmployee(strToken,strName,strMobile,strEmail,strPassword,strAddress)

        }else{
            btAddEmpl.visibility+View.VISIBLE
            pbAddEmpl.visibility=View.GONE
            AlertHelper.showNoInternetSnackBar(this@AddEmployeeActivity, object :
                AlertHelper.SnackBarListener {
                override fun onOkClick() {
                    btAddEmpl.visibility+View.GONE
                    pbAddEmpl.visibility=View.VISIBLE
                    presenter.addEmployee(strToken,strName,strMobile,strEmail,strPassword,strAddress)
                }
            })
        }


    }

    override fun onaddEmployeeSuccess(apiResponse: DefaultResponse) {
        pbAddEmpl.visibility=View.GONE
        btAddEmpl.visibility+View.VISIBLE
        AlertHelper.showOKSnackBarAlert(this@AddEmployeeActivity,"Employee Added Successfully")
        etName.text.clear()
        etMobile.text.clear()
        etEmail.text.clear()
        etPassword.text.clear()
        etAddress.text.clear()

    }

    override fun onaddEmployeeNull(apiResponse: DefaultResponse) {
        pbAddEmpl.visibility=View.GONE
        btAddEmpl.visibility+View.VISIBLE
    }

    override fun onaddEmployeeFailed(apiResponse: JSONArray) {
        pbAddEmpl.visibility=View.GONE
        btAddEmpl.visibility+View.VISIBLE

        val listData = ArrayList<String>()
        for (i in 0 until apiResponse.length()) {
            listData += apiResponse.getString(i)
            Log.e("ListData",listData.toString())
            for(i in listData){
                if(i=="USER NAME ALREADY EXIST"){
                    etName.error = "USER NAME ALREADY EXIST *"
                    etName.requestFocus()
                    pbAddEmpl.visibility=View.GONE
                    btAddEmpl.visibility+View.VISIBLE
                }else if(i=="MOBILE NUMBER ALREADY EXIST"){
                    etMobile.error = "MOBILE NUMBER ALREADY EXIST *"
                    etMobile.requestFocus()
                    pbAddEmpl.visibility=View.GONE
                    btAddEmpl.visibility+View.VISIBLE
                }
                else if(i=="EMAIL ALREADY EXIST"){
                    etEmail.error = "EMAIL ALREADY EXIST *"
                    etEmail.requestFocus()
                    pbAddEmpl.visibility=View.GONE
                    btAddEmpl.visibility+View.VISIBLE
                }
            }
        }
       // AlertHelper.showOKSnackBarAlert(this@AddEmployeeActivity,"Employee Adding Failed")
    }

    override fun onaddEmployeeFailedServerError(string: String) {
        pbAddEmpl.visibility=View.GONE
        btAddEmpl.visibility+View.VISIBLE
        AlertHelper.showNoInternetSnackBar(this@AddEmployeeActivity, object :
            AlertHelper.SnackBarListener {
            override fun onOkClick() {
            }
        })
    }



    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}