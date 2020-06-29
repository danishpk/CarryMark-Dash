package tech.stacka.carrymarkdashboard.activity.home

import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class HomePresenter(homeView: HomeView, mContext: Context) {
    private val homeView: HomeView = homeView
    private val mContext: Context = mContext

//    fun productList() {
//        val retrofitClient=RetrofitClient(EndPoint.baseUrl2)
//        val apiResponseCall: Call<ProductResponse> =
//           // RetrofitClient.instance.productList()
//            retrofitClient.instance.productList()
//        apiResponseCall.enqueue(object : Callback<ProductResponse> {
//            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
//                val apiResponse: ProductResponse = response.body()!!
//                if (response.isSuccessful()) {
//                    if (response.body()!!.blnAPIStatus) {
//                        homeView.onHomeDataSuccess(apiResponse)
//                    } else {
//                        homeView.onHomeDataFailed(apiResponse)
//                    }
//                } else {
//                    homeView.onHomeDataFailed(apiResponse)
//                }
//            }
//            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
//                Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show()
//                homeView.onHomeDataFailedServerError(mContext.getString(R.string.server_error))
//            }
//        })
//    }
}