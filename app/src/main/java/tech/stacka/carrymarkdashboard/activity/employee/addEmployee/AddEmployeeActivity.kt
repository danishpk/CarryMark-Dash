package tech.stacka.carrymarkdashboard.activity.employee.addEmployee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_employee.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager

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
        presenter.addEmployee(strToken,strName,strMobile,strEmail,strPassword)

    }

    override fun onaddEmployeeSuccess(apiResponse: DefaultResponse) {
        Toast.makeText(applicationContext,apiResponse.strMessage,Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onaddEmployeeNull(apiResponse: DefaultResponse) {
        TODO("Not yet implemented")
    }

    override fun onaddEmployeeFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,apiResponse.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun onaddEmployeeFailedServerError(string: String) {
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }
}