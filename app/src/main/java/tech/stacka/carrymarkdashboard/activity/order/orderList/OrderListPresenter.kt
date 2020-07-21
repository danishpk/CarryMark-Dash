package tech.stacka.carrymarkdashboard.activity.order.orderList

import android.content.Context
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.models.OrderListResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class OrderListPresenter(val orderListView: OrderListView,val context: Context) {

    fun orderList(strToken:String,arrExecutiveId:ArrayList<String>,arrDistributerId:ArrayList<String>,arrStatus:ArrayList<String>) {
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)

        val objOrderList=JsonObject()
        val arrExecIds=JsonArray()
        val arrDistrIds=JsonArray()
        val arrStatusIds=JsonArray()

        for(i in arrExecutiveId){
            arrExecIds.add(i)
        }

        for(i in arrDistributerId){
            arrDistrIds.add(i)
        }

        for(i in arrStatus){
            arrStatusIds.add(i)
        }

        objOrderList.add("arrExecutiveId",arrExecIds)
        objOrderList.add("arrDistributerId",arrDistrIds)
        objOrderList.add("arrStatus",arrStatusIds)
        val apiResponseCall: Call<OrderListResponse> = retrofitClient.instance.orderList(strToken,objOrderList)
        apiResponseCall.enqueue(object : Callback<OrderListResponse> {
            override fun onResponse(call: Call<OrderListResponse>, response: Response<OrderListResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: OrderListResponse? = response.body()
                    if (apiResponse != null) {
                        orderListView.onOrderListSuccess(apiResponse)
                    }else{
                        val apiResponse: OrderListResponse = response.body()!!
                        orderListView.onOrderListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    orderListView.onOrderListFailed(apiResponse)
                }
            }
            override fun onFailure(call: Call<OrderListResponse>, t: Throwable) {
                //    Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                orderListView.onOrderServerFailed(t.toString())
            }
        })
    }
}