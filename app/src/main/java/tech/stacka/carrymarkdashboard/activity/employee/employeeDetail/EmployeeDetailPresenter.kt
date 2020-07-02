package tech.stacka.carrymarkdashboard.activity.employee.employeeDetail

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.EmployeeDetailResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class EmployeeDetailPresenter(val employeeDetailView: EmployeeDetailView,val context: Context) {


    fun employeeDetails(strToken:String,strEmpoyeeId:String) {
        val objEmployeeList= JsonObject()
        objEmployeeList.addProperty("strType","EMPLOYEE")
        objEmployeeList.addProperty("strDocId",strEmpoyeeId)
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<EmployeeDetailResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.employeeDetail(strToken,objEmployeeList)
        apiResponseCall.enqueue(object : Callback<EmployeeDetailResponse> {
            override fun onResponse(call: Call<EmployeeDetailResponse>, response: Response<EmployeeDetailResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: EmployeeDetailResponse? = response.body()
                    if (apiResponse != null) {
                        employeeDetailView.onEmployeeDetailSuccess(apiResponse)
                    }else{
                        val apiResponse: EmployeeDetailResponse = response.body()!!
                        employeeDetailView.onEmployeeDetailNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        employeeDetailView.onEmployeeDetailFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<EmployeeDetailResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                employeeDetailView.onEmployeeDetailFailedServerError(context.getString(R.string.server_error))
            }
        })
    }
}