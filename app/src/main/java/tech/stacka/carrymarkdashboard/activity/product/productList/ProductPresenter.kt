package tech.stacka.carrymarkdashboard.activity.product.productList

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.ProductListResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class ProductPresenter(productView: ProductView, mContext: Context) {
    private val productView: ProductView = productView
    private val mContext: Context = mContext

    fun productList(strSort:String,strSortActive:String,arrFilterBrand:ArrayList<String>,
                    arrFilterCategory:ArrayList<String>,arrFilterMaterial:ArrayList<String>,offset:Int,limit:Int) {
        val retrofitClient=RetrofitClient(EndPoint.baseUrl1)
        val objProject=JsonObject()
        val arrFilteredBrand=JsonArray()
        val arrFilteredCategory=JsonArray()
        val arrFilteredMaterial=JsonArray()
        for(i in arrFilterBrand){
            arrFilteredBrand.add(i)
        }
        for (k in arrFilterCategory){
            arrFilteredCategory.add(k)
        }

        for (j in arrFilterMaterial){
            arrFilteredMaterial.add(j)
        }
        objProject.addProperty("intPageNo",offset)
        objProject.addProperty("intLimit",limit)
        objProject.addProperty("strSort",strSort)
        objProject.addProperty("strSortActive",strSortActive)
        objProject.add("arrBrands",arrFilteredBrand)
        objProject.add("arrCategory",arrFilteredCategory)
        objProject.add("arrMaterial",arrFilteredCategory)


        val apiResponseCall: Call<ProductListResponse> =
          //  RetrofitClient.instance.productList()
            retrofitClient.instance.productList(objProject)
        apiResponseCall.enqueue(object : Callback<ProductListResponse> {
            override fun onResponse(call: Call<ProductListResponse>, response: Response<ProductListResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: ProductListResponse? = response.body()
                    if (apiResponse != null) {
                        productView.onProductListSuccess(apiResponse)
                    }else{
                        val apiResponse: ProductListResponse = response.body()!!
                        productView.onProductListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    productView.onProductListFailed(apiResponse)
                }
            }
            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show()
                productView.onProductListFailedServerError(mContext.getString(R.string.server_error))
            }
        })
    }

    fun performPagination(strSort:String,strSortActive:String,arrFilterBrand:ArrayList<String>,
                          arrFilterCategory:ArrayList<String>,arrFilterMaterial:ArrayList<String>,offset:Int,limit:Int) {
        val retrofitClient=RetrofitClient(EndPoint.baseUrl1)
        val objProject=JsonObject()
        val arrFilteredBrand=JsonArray()
        val arrFilteredCategory=JsonArray()
        val arrFilteredMaterial=JsonArray()
        for(i in arrFilterBrand){
            arrFilteredBrand.add(i)
        }
        for (k in arrFilterCategory){
            arrFilteredCategory.add(k)
        }
        for (j in arrFilterMaterial){
            arrFilteredMaterial.add(j)
        }

        objProject.addProperty("strSort",strSort)
        objProject.addProperty("strSortActive",strSortActive)
        objProject.addProperty("intPageNo",offset)
        objProject.addProperty("intLimit",limit)
        objProject.add("arrBrands",arrFilteredBrand)
        objProject.add("arrCategory",arrFilteredCategory)
        objProject.add("arrMaterial",arrFilteredCategory)


        val apiResponseCall: Call<ProductListResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.productList(objProject)
        apiResponseCall.enqueue(object : Callback<ProductListResponse> {
            override fun onResponse(call: Call<ProductListResponse>, response: Response<ProductListResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: ProductListResponse? = response.body()
                    if (apiResponse != null) {
                        productView.onProductListMoreSuccess(apiResponse)
                    }else{
                        val apiResponse: ProductListResponse = response.body()!!
                        productView.onProductListMoreNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    productView.onProductListMoreFailed(apiResponse)
                }
            }
            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show()
                productView.onProductListFailedMoreServerError(mContext.getString(R.string.server_error))
            }
        })
    }

    fun deleteProduct(strToken:String,strProductId:String) {
        val objDeleteProduct= JsonObject()
        val arrDelProduct= JsonArray()
        arrDelProduct.add(strProductId)
        objDeleteProduct.add("arrDeleteId",arrDelProduct)
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<DefaultResponse> = retrofitClient.instance.deleteProduct(strToken,objDeleteProduct)
        apiResponseCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: DefaultResponse? = response.body()
                    if (apiResponse != null) {
                        productView.onDeleteProductSuccess(apiResponse)
                    }else{
                        val apiResponse: DefaultResponse = response.body()!!
                        productView.onDeleteProductNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    productView.onDeleteProductFailed(apiResponse)
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(mContext,"error", Toast.LENGTH_LONG).show()
                productView.onDeleteProductFailedServerError(t.toString())
            }
        })
    }



}