package tech.stacka.carrymarkdashboard.activity.map

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.EmployeeDetailResponse
import tech.stacka.carrymarkdashboard.models.EmployeeLocationResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class MapPresenter(val mapView: MapView,val context: Context) {

    fun employeeLocations(strToken:String,strEmployeeId:String,strDate:String) {
        val objEmployeeList= JsonObject()
        objEmployeeList.addProperty("strCreatedTime",strDate)
        objEmployeeList.addProperty("strExecutiveId",strEmployeeId)
        val retrofitClient= RetrofitClient(EndPoint.baseUrl3)
        val apiResponseCall: Call<EmployeeLocationResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.getLocation(strToken,objEmployeeList)
        apiResponseCall.enqueue(object : Callback<EmployeeLocationResponse> {
            override fun onResponse(call: Call<EmployeeLocationResponse>, response: Response<EmployeeLocationResponse>) {
                if (response.isSuccessful) {
                    val apiResponse: EmployeeLocationResponse? = response.body()
                    if (apiResponse != null) {
                        mapView.onEmployeeLocationSuccess(apiResponse)
                    }else{
                        val apiResponse: EmployeeLocationResponse = response.body()!!
                        mapView.onEmployeeLocationNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        mapView.onEmployeeLocationFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<EmployeeLocationResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                mapView.onEmployeeLocationFailedServerError(context.getString(R.string.server_error))
            }
        })
    }
}