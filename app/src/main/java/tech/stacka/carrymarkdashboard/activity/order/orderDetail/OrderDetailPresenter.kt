package tech.stacka.carrymarkexecutive.activity.order.orderDetail

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.ErrCommon
import tech.stacka.carrymarkdashboard.models.OrderDetailResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class OrderDetailPresenter(val orderDetailView: OrderDetailView,val context: Context) {

    fun orderDetails(strToken:String,strOrderId:String) {
        val objOrderDetail=JsonObject()
        objOrderDetail.addProperty("strOrderId",strOrderId)

        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<OrderDetailResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.orderDetail(strToken,objOrderDetail)
        apiResponseCall.enqueue(object : Callback<OrderDetailResponse> {
            override fun onResponse(call: Call<OrderDetailResponse>, response: Response<OrderDetailResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: OrderDetailResponse? = response.body()
                    if (apiResponse != null) {
                        orderDetailView.onOrderDetailSuccess(apiResponse)
                    }else{
                        val apiResponse: OrderDetailResponse = response.body()!!
                        orderDetailView.onOrderDetailNull(apiResponse)
                    }
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrCommon>() {}.type
                    val apiResponse: ErrCommon? = gson.fromJson(response.errorBody()!!.charStream(),type)
                    if (apiResponse != null) {
                        orderDetailView.onOrderDetailFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {
                //    Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                orderDetailView.onOrderServerFailed(t.toString())
            }
        })
    }

    fun updateOrder(strToken:String,strOrderId:String,strOrderStatus:String) {
        val objUpdateOrder= JsonObject()
        objUpdateOrder.addProperty("strOrderId",strOrderId)
        objUpdateOrder.addProperty("strOrderStatus",strOrderStatus)
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<DefaultResponse> = retrofitClient.instance.orderUpdate(strToken,objUpdateOrder)
        apiResponseCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: DefaultResponse? = response.body()
                    if (apiResponse != null) {
                        orderDetailView.onOrderUpdateSuccess(apiResponse)
                    }else{
                        val apiResponse: DefaultResponse = response.body()!!
                        orderDetailView.onOrderUpdateNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    orderDetailView.onOrderUpdateFailed(apiResponse)
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                orderDetailView.onOrderUpdateFailedServerError(t.toString())
            }
        })
    }
}