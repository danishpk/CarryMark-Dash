package tech.stacka.carrymarkdashboard.activity.product.filterProduct

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.FilterCategoryResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class ProductFilterPresenter(productFilterView: ProductFilterView, mContext: Context) {

    private val productFilterView: ProductFilterView = productFilterView
    private val mContext: Context = mContext


    fun filtercategoryList(arrCategory: ArrayList<String>, intLimit:Int) {
        val retrofitClient= RetrofitClient(EndPoint.baseUrl1)
        val objcategory= JsonObject()
        val arrCategoryList= JsonArray()

        for(String in arrCategory){
            val objCollection= JsonObject()
            objCollection.addProperty("strCollection",String)
            objCollection.addProperty("intLimit",intLimit)
            arrCategoryList.add(objCollection)
        }
        objcategory.add("arrCollection",arrCategoryList)
        val apiResponseCall: Call<FilterCategoryResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.filterCategoryList(objcategory)
        apiResponseCall.enqueue(object : Callback<FilterCategoryResponse> {
            override fun onResponse(call: Call<FilterCategoryResponse>, response: Response<FilterCategoryResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: FilterCategoryResponse? = response.body()
                    if (apiResponse != null) {
                        productFilterView.onFilterCategoryListSuccess(apiResponse)
                    }else{
                        val apiResponse: FilterCategoryResponse = response.body()!!
                        productFilterView.onFilterProductCategoryListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    productFilterView.onFilterProductCategoryListFailed(apiResponse)
                }
            }
            override fun onFailure(call: Call<FilterCategoryResponse>, t: Throwable) {
                Toast.makeText(mContext,"error", Toast.LENGTH_LONG).show()
                productFilterView.onFilterProductCategoryServerFailed(mContext.getString(R.string.server_error))
            }
        })
    }
}