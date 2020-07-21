package tech.stacka.carrymarkdashboard.activity.order.orderFilter

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DistributerListResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class OrderFilterPresenter(val orderFilterView: OrderFilterView,val context: Context) {

    fun distributerList(strToken:String) {
        val objDistributerList= JsonObject()
        objDistributerList.addProperty("strType","DISTRIBUTER")
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<DistributerListResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.distributerList(strToken,objDistributerList)
        apiResponseCall.enqueue(object : Callback<DistributerListResponse> {
            override fun onResponse(call: Call<DistributerListResponse>, response: Response<DistributerListResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: DistributerListResponse? = response.body()
                    if (apiResponse != null) {
                        orderFilterView.onDistributerListSuccess(apiResponse)
                    }else{
                        val apiResponse: DistributerListResponse = response.body()!!
                        orderFilterView.onDistributerListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        orderFilterView.onDistributerListFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<DistributerListResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                orderFilterView.onDistributerListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }


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
                        orderFilterView.onEmployeeListSuccess(apiResponse)
                    }else{
                        val apiResponse: EmployeeListResponse = response.body()!!
                        orderFilterView.onEmployeeListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        orderFilterView.onEmployeeListFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<EmployeeListResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                orderFilterView.onEmployeeListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }
}