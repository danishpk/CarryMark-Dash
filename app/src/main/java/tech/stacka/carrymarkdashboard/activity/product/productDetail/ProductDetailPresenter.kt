package tech.stacka.carrymarkdashboard.activity.product.productDetail

import android.content.Context
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.models.ProductDetailResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class ProductDetailPresenter(val productDetailView: ProductDetailView,val context: Context) {

    fun productDetails(strToken: String, strProductId: String) {
        val retrofitClient = RetrofitClient(EndPoint.baseUrl1)
        val objProductId = JsonObject()
        objProductId.addProperty("strProductId", strProductId)
        val apiResponseCall: Call<ProductDetailResponse> =
            retrofitClient.instance.productDetail(strToken, objProductId)
        apiResponseCall.enqueue(object : Callback<ProductDetailResponse> {
            override fun onResponse(
                call: Call<ProductDetailResponse>,
                response: Response<ProductDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse: ProductDetailResponse? = response.body()
                    if (apiResponse != null) {
                        productDetailView.onProductDetailSuccess(apiResponse)
                    } else {
                        val apiResponse: ProductDetailResponse = response.body()!!
                        productDetailView.onProductDetailNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    productDetailView.onProductDetailFailed(apiResponse)
                }
            }

            override fun onFailure(call: Call<ProductDetailResponse>, t: Throwable) {
                //    Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                productDetailView.onProductDetailServerFailed(t.toString())
            }
        })
    }
}