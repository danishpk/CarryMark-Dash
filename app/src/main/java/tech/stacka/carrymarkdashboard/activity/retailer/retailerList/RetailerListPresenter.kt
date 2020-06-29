package tech.stacka.carrymarkdashboard.activity.retailer.retailerList

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.RetailerListResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class RetailerListPresenter(retailreListView: RetailerListView,context: Context) {

    private val retailerListView: RetailerListView =retailreListView
    private val context: Context =context


    fun retailerList(strToken:String) {
        val objRetailerList= JsonObject()
        objRetailerList.addProperty("strType","RETAILER")
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<RetailerListResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.retailerList(strToken,objRetailerList)
        apiResponseCall.enqueue(object : Callback<RetailerListResponse> {
            override fun onResponse(call: Call<RetailerListResponse>, response: Response<RetailerListResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: RetailerListResponse? = response.body()
                    if (apiResponse != null) {
                        retailerListView.onRetailerListSuccess(apiResponse)
                    }else{
                        val apiResponse: RetailerListResponse = response.body()!!
                        retailerListView.onRetailerListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    retailerListView.onRetailerListFailed(apiResponse)
                }
            }
            override fun onFailure(call: Call<RetailerListResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                retailerListView.onRetailerListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }
}