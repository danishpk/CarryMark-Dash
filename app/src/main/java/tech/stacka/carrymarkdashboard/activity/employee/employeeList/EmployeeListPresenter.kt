package tech.stacka.carrymarkdashboard.activity.employee.employeeList

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class EmployeeListPresenter(employeeListView: EmployeeListView,context: Context) {

    private val employeeListView:EmployeeListView=employeeListView
    private val context:Context=context


    fun employeeList(strToken:String) {
        val objEmployeeList=JsonObject()
        objEmployeeList.addProperty("strType","EMPLOYEE")
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<EmployeeListResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.employeeList(strToken,objEmployeeList)
        apiResponseCall.enqueue(object : Callback<EmployeeListResponse> {
            override fun onResponse(call: Call<EmployeeListResponse>, response: Response<EmployeeListResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: EmployeeListResponse? = response.body()
                    if (apiResponse != null) {
                        employeeListView.onEmployeeListSuccess(apiResponse)
                    }else{
                        val apiResponse: EmployeeListResponse = response.body()!!
                        employeeListView.onEmployeeListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        employeeListView.onEmployeeListFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<EmployeeListResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                employeeListView.onEmployeeListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }


}