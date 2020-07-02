package tech.stacka.carrymarkdashboard.activity.product.addProduct

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.AddProductCategoryResponse
import tech.stacka.carrymarkdashboard.models.AddProductResponse
import tech.stacka.carrymarkdashboard.models.AddSizeColorResponse
import tech.stacka.carrymarkdashboard.models.UploadProductImageResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient
import java.io.File


class AddProductPresenter(addProductView: AddProductView, mContext: Context) {
    private val addProductView: AddProductView = addProductView
    private val mContext: Context = mContext
    fun addProduct(
        strName: String,
        strProductId: String,
        strDescription: String,
        strCategoryId: String,
        strBrandId: String,
        strGenderCategory: String,
        dblMRP: String,
        dblSellingPrice: String,
        dblRetailerPrice: String,
        strTargetType: String,
        dblTotalStock: String,
        intTotalSales: String,
        intEstiDeliveryDays: String,
        strUnit: String,
        arrImageUrl: JsonArray,
        strToken: String,
        strMaterial:String,
        arrSelectedSize: MutableList<String>,
        arrColorSelected: JsonArray,
        strSubCategory:String,
        arrSelectedColorCode: MutableList<String>
    ) {
        val arrSchemeJson = JsonArray()
        val arrSizeStockJson=JsonArray()
        for(i in arrSelectedSize){
            val objsize=JsonObject()
            objsize.addProperty("strName",i)
            arrSizeStockJson.add(objsize)
        }
        val schemeDataJson=JsonObject()
        val addProductJson = JsonObject()
        addProductJson.addProperty("strName",strName)
        addProductJson.addProperty("strProductId",strProductId)
        addProductJson.addProperty("strDescription",strDescription)
        addProductJson.addProperty("strCategoryId",strCategoryId)
        addProductJson.addProperty("strBrandId",strBrandId)
        addProductJson.addProperty("strGenderCategory",strGenderCategory)
        addProductJson.addProperty("dblMRP",dblMRP)
        addProductJson.addProperty("dblSellingPrice",dblSellingPrice)
        addProductJson.addProperty("dblRetailerPrice",dblRetailerPrice)
        addProductJson.add("arrScheme",arrSchemeJson)
        addProductJson.addProperty("strTargetType",strTargetType)
        addProductJson.addProperty("dblTotalStock",dblTotalStock)
        addProductJson.addProperty("intTotalSales",intTotalSales)
        addProductJson.addProperty("intEstiDeliveryDays",intEstiDeliveryDays)
        addProductJson.addProperty("strUnit",strUnit)
        addProductJson.addProperty("strMaterial",strMaterial)
        addProductJson.add("arrImageUrl",arrImageUrl)
        addProductJson.add("arrSizeStock",arrSizeStockJson)
        addProductJson.add("arrColorStock",arrColorSelected)
        addProductJson.addProperty("strSubCategory",strSubCategory)
        val retrofitClient=RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<AddProductResponse> = retrofitClient.instance.addProduct(strToken,addProductJson)
        apiResponseCall.enqueue(object : Callback<AddProductResponse> {
            override fun onResponse(call: Call<AddProductResponse>, response: Response<AddProductResponse>) {

                if (response.isSuccessful()) {
                    val apiResponse: AddProductResponse? = response.body()
                    if (apiResponse != null) {
                        addProductView.addProductSuccess(apiResponse)
                    }else{
                        val apiResponse: AddProductResponse = response.body()!!
                        addProductView.addProductNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        addProductView.addProductFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<AddProductResponse>, t: Throwable) {
                Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show()
                addProductView.addProductFailedServerError(t.toString())
            }
        })
    }

    fun uploadImage( paths:List<File>,strToken:String) {
        val retrofitClient=RetrofitClient(EndPoint.baseUrl2)
        val list: MutableList<MultipartBody.Part> = ArrayList()
        for (file in paths) {
            val imageRequest: MultipartBody.Part = prepareMultipartpartBody(file.name, file)
            list.add(imageRequest)
        }
        val apiResponseCall: Call<UploadProductImageResponse> = retrofitClient.instance.uploadProductImage(strToken,list)
        apiResponseCall.enqueue(object : Callback<UploadProductImageResponse> {
            override fun onResponse(call: Call<UploadProductImageResponse>, response: Response<UploadProductImageResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: UploadProductImageResponse? = response.body()
                    if (apiResponse != null) {
                        addProductView.uploadImageSuccess(apiResponse)
                    }else{
                        val apiResponse: UploadProductImageResponse = response.body()!!
                        addProductView.uploadImageNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        addProductView.uploadImageFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<UploadProductImageResponse>, t: Throwable) {
                Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show()
                addProductView.uploadImageFailedServerError(t.toString()
                 //   mContext.getString(R.string.server_error)
                )
            }
        })
    }


    fun categoryList(strCollection:String,strValue:String,strToken: String) {
        val retrofitClient=RetrofitClient(EndPoint.baseUrl1)
        val objcategory= JsonObject()
        objcategory.addProperty("strCollection",strCollection)
        objcategory.addProperty("strValue",strValue)
        val apiResponseCall: Call<AddProductCategoryResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.addProductCategoryList(strToken,objcategory)
        apiResponseCall.enqueue(object : Callback<AddProductCategoryResponse> {
            override fun onResponse(call: Call<AddProductCategoryResponse>, response: Response<AddProductCategoryResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: AddProductCategoryResponse? = response.body()
                    if (apiResponse != null) {
                        addProductView.onProductCategoryListSuccess(apiResponse)
                    }else{
                        val apiResponse: AddProductCategoryResponse = response.body()!!
                        addProductView.onProductCategoryListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        addProductView.onProductCategoryListFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<AddProductCategoryResponse>, t: Throwable) {
                Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show()
                addProductView.onProductCategoryListFailedServerError(mContext.getString(R.string.server_error))
            }
        })
    }



    fun subCategoryList(strCollection:String,strValue:String,strToken: String,strParentCategory:String) {
        val retrofitClient=RetrofitClient(EndPoint.baseUrl1)
        val objsubcategory= JsonObject()
        objsubcategory.addProperty("strCollection",strCollection)
        objsubcategory.addProperty("strValue",strValue)
        val objMainCategory=JsonObject()
        objMainCategory.addProperty("strParentCategory",strParentCategory)
        objsubcategory.add("objCondition",objMainCategory)
        val apiResponseCall: Call<AddProductCategoryResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.addProductCategoryList(strToken,objsubcategory)
        apiResponseCall.enqueue(object : Callback<AddProductCategoryResponse> {
            override fun onResponse(call: Call<AddProductCategoryResponse>, response: Response<AddProductCategoryResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: AddProductCategoryResponse? = response.body()
                    if (apiResponse != null) {
                        addProductView.onProductCategoryListSuccess(apiResponse)
                    }else{
                        val apiResponse: AddProductCategoryResponse = response.body()!!
                        addProductView.onProductCategoryListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        addProductView.onProductCategoryListFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<AddProductCategoryResponse>, t: Throwable) {
                Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show()
                addProductView.onProductCategoryListFailedServerError(mContext.getString(R.string.server_error))
            }
        })
    }


    fun addSizeColor(arrData: ArrayList<String>, intLimit:Int) {
        val retrofitClient=RetrofitClient(EndPoint.baseUrl1)
        val objcategory= JsonObject()
        val arrCategoryList= JsonArray()
        for(String in arrData){
            val objCollection= JsonObject()
            objCollection.addProperty("strCollection",String)
            objCollection.addProperty("intLimit",intLimit)
            arrCategoryList.add(objCollection)
        }
        objcategory.add("arrCollection",arrCategoryList)
        val apiResponseCall: Call<AddSizeColorResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.addSizeColor(objcategory)
        apiResponseCall.enqueue(object : Callback<AddSizeColorResponse> {
            override fun onResponse(call: Call<AddSizeColorResponse>, response: Response<AddSizeColorResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: AddSizeColorResponse? = response.body()
                    if (apiResponse != null) {
                        addProductView.onAddSizeColorSuccess(apiResponse)
                    }else{
                        val apiResponse: AddSizeColorResponse = response.body()!!
                        addProductView.onAddSizeColorNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        addProductView.onAddSizeColorFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<AddSizeColorResponse>, t: Throwable) {
                Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show()
                addProductView.onAddSizeColorFailedServerError(mContext.getString(R.string.server_error))
            }
        })
    }

    private fun prepareMultipartpartBody(
        partName: String,
        file: File
    ): MultipartBody.Part {
        var requestBody = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData("images", file.getName(), requestBody)
    }
}