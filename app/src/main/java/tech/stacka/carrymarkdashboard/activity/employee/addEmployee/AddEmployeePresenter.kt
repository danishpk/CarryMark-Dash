package tech.stacka.carrymarkdashboard.activity.employee.addEmployee

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class AddEmployeePresenter(private val addEmployeeView: AddEmployeeView,
                           private val context: Context) {

    fun addEmployee(strToken:String,strName:String,strMobile:String,strEmail:String,strPassword:String) {
        val objAddEmployee= JsonObject()
        val arrAddress=JsonArray()
        objAddEmployee.addProperty("strName",strName)
        objAddEmployee.addProperty("strMobileNo",strMobile)
        objAddEmployee.addProperty("strEmail",strEmail)
        objAddEmployee.addProperty("strPassword",strPassword)
        objAddEmployee.add("arrAddress",arrAddress)
        objAddEmployee.addProperty("strType","EMPLOYEE")
        val retrofitClient= RetrofitClient(EndPoint.baseUrl1)
        val apiResponseCall: Call<DefaultResponse> = retrofitClient.instance.createUser(strToken,objAddEmployee)
        apiResponseCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: DefaultResponse? = response.body()
                    if (apiResponse != null) {
                        addEmployeeView.onaddEmployeeSuccess(apiResponse)
                    }else{
                        val apiResponse: DefaultResponse = response.body()!!
                        addEmployeeView.onaddEmployeeNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        addEmployeeView.onaddEmployeeFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                addEmployeeView.onaddEmployeeFailedServerError(t.toString())
            }
        })
    }
}