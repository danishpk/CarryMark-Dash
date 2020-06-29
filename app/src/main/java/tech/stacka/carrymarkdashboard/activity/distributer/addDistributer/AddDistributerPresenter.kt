package tech.stacka.carrymarkdashboard.activity.distributer.addDistributer

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class AddDistributerPresenter(private val addDistributerView: AddDistributerView,
                              private val context: Context) {

    fun addDistributer(strToken:String,strName:String,strMobile:String,strEmail:String,strPassword:String) {
        val objAddEmployee= JsonObject()
        val arrAddress=JsonArray()
        val arrExecutive=JsonArray()
        objAddEmployee.addProperty("strName",strName)
        objAddEmployee.addProperty("strMobileNo",strMobile)
        objAddEmployee.addProperty("strEmail",strEmail)
        objAddEmployee.addProperty("strPassword",strPassword)
        objAddEmployee.add("arrAddress",arrAddress)
        objAddEmployee.addProperty("strType","DISTRIBUTER")
        objAddEmployee.add("arrExecutiveId",arrExecutive)
        val retrofitClient= RetrofitClient(EndPoint.baseUrl1)
        val apiResponseCall: Call<DefaultResponse> = retrofitClient.instance.createUser(strToken,objAddEmployee)
        apiResponseCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: DefaultResponse? = response.body()
                    if (apiResponse != null) {
                        addDistributerView.onaddDistributerSuccess(apiResponse)
                    }else{
                        val apiResponse: DefaultResponse = response.body()!!
                        addDistributerView.onaddDistributerNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    addDistributerView.onaddDistributerFailed(apiResponse)
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                addDistributerView.onaddDistributerFailedServerError(t.toString())
            }
        })
    }
}