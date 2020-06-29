package tech.stacka.carrymarkdashboard.activity.distributer.addDistributer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_employee.*
import kotlinx.android.synthetic.main.toolbar_child.*
import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.storage.SharedPrefManager

class AddDistributerActivity : AppCompatActivity(), AddDistributerView {

    var strToken:String = ""
    val presenter= AddDistributerPresenter(this,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_distributer)

        strToken= SharedPrefManager.getInstance(applicationContext).user.strToken!!
        nav_back.setOnClickListener {
            finish()
        }
    }

    fun btAddDistbtrClick() {

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
        presenter.addDistributer(strToken,strName,strMobile,strEmail,strPassword)
    }
    override fun onaddDistributerSuccess(apiResponse: DefaultResponse) {
        Toast.makeText(applicationContext,apiResponse.strMessage, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onaddDistributerNull(apiResponse: DefaultResponse) {
        TODO("Not yet implemented")
    }

    override fun onaddDistributerFailed(apiResponse: ResponseBody) {
        Toast.makeText(applicationContext,apiResponse.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun onaddDistributerFailedServerError(string: String) {
        Toast.makeText(applicationContext,string,Toast.LENGTH_SHORT).show()
    }


}