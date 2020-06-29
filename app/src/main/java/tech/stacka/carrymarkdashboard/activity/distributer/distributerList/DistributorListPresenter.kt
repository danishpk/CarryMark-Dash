package tech.stacka.carrymarkdashboard.activity.distributer.distributerList

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DistributerListResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class DistributorListPresenter(distributorListView: DistributorListView, context: Context) {


    private val distributorListView: DistributorListView =distributorListView
    private val context: Context =context


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
                        distributorListView.onDistributerListSuccess(apiResponse)
                    }else{
                        val apiResponse: DistributerListResponse = response.body()!!
                        distributorListView.onDistributerListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        distributorListView.onDistributerListFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<DistributerListResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                distributorListView.onDistributerListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }
}