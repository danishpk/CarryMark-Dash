package tech.stacka.carrymarkdashboard.activity.product.productDetails

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
import tech.stacka.carrymarkdashboard.models.*
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient
import java.io.File

class ProductDetailPresenter(val productDetailView: ProductDetailView, val context: Context) {

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
                if (response.isSuccessful()) {
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


    fun updateProduct(
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
        strMaterial: String,
        arrSelectedSize: MutableList<String>,
        arrColorSelected: JsonArray,
        strSubCategory: String,
        arrSelectedColorCode: MutableList<String>,
        strDocId:String
    ) {
        val arrSchemeJson = JsonArray()
        val arrSizeStockJson = JsonArray()
        for (i in arrSelectedSize) {
            val objsize = JsonObject()
            objsize.addProperty("strName", i)
            arrSizeStockJson.add(objsize)
        }
        val schemeDataJson = JsonObject()
        val addProductJson = JsonObject()
        addProductJson.addProperty("strName", strName)
        addProductJson.addProperty("strProductId", strProductId)
        addProductJson.addProperty("strDescription", strDescription)
        addProductJson.addProperty("strCategoryId", strCategoryId)
        addProductJson.addProperty("strBrandId", strBrandId)
        addProductJson.addProperty("strGenderCategory", strGenderCategory)
        addProductJson.addProperty("dblMRP", dblMRP)
        addProductJson.addProperty("dblSellingPrice", dblSellingPrice)
        addProductJson.addProperty("dblRetailerPrice", dblRetailerPrice)
        addProductJson.add("arrScheme", arrSchemeJson)
        addProductJson.addProperty("strTargetType", strTargetType)
        addProductJson.addProperty("dblTotalStock", dblTotalStock)
        addProductJson.addProperty("intTotalSales", intTotalSales)
        addProductJson.addProperty("intEstiDeliveryDays", intEstiDeliveryDays)
        addProductJson.addProperty("strUnit", strUnit)
        addProductJson.addProperty("strMaterial", strMaterial)
        addProductJson.add("arrImageUrl", arrImageUrl)
        addProductJson.add("arrSizeStock", arrSizeStockJson)
        addProductJson.add("arrColorStock", arrColorSelected)
        addProductJson.addProperty("strSubCategory", strSubCategory)
        addProductJson.addProperty("strDocId", strDocId)
        addProductJson.addProperty("strOperationType", "UPDATE")
        val retrofitClient = RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<AddProductResponse> =
            retrofitClient.instance.addProduct(strToken, addProductJson)
        apiResponseCall.enqueue(object : Callback<AddProductResponse> {
            override fun onResponse(
                call: Call<AddProductResponse>,
                response: Response<AddProductResponse>
            ) {

                if (response.isSuccessful()) {
                    val apiResponse: AddProductResponse? = response.body()
                    if (apiResponse != null) {
                        productDetailView.updateProductSuccess(apiResponse)
                    } else {
                        val apiResponse: AddProductResponse = response.body()!!
                        productDetailView.updateProductNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if (apiResponse != null) {
                        productDetailView.updateProductFailed(apiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<AddProductResponse>, t: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                productDetailView.updateProductFailedServerError(t.toString())
            }
        })
    }

    fun uploadImage(paths: List<File>, strToken: String) {
        val retrofitClient = RetrofitClient(EndPoint.baseUrl2)
        val list: MutableList<MultipartBody.Part> = ArrayList()
        for (file in paths) {
            val imageRequest: MultipartBody.Part = prepareMultipartpartBody(file.name, file)
            list.add(imageRequest)
        }
        val apiResponseCall: Call<UploadProductImageResponse> =
            retrofitClient.instance.uploadProductImage(strToken, list)
        apiResponseCall.enqueue(object : Callback<UploadProductImageResponse> {
            override fun onResponse(
                call: Call<UploadProductImageResponse>,
                response: Response<UploadProductImageResponse>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: UploadProductImageResponse? = response.body()
                    if (apiResponse != null) {
                        productDetailView.uploadImageSuccess(apiResponse)
                    } else {
                        val apiResponse: UploadProductImageResponse = response.body()!!
                        productDetailView.uploadImageNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if (apiResponse != null) {
                        productDetailView.uploadImageFailed(apiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<UploadProductImageResponse>, t: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                productDetailView.uploadImageFailedServerError(
                    t.toString()
                    //   mContext.getString(R.string.server_error)
                )
            }
        })
    }


    fun categoryList(strCollection: String, strValue: String, strToken: String) {
        val retrofitClient = RetrofitClient(EndPoint.baseUrl1)
        val objcategory = JsonObject()
        objcategory.addProperty("strCollection", strCollection)
        objcategory.addProperty("strValue", strValue)
        val apiResponseCall: Call<AddProductCategoryResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.addProductCategoryList(strToken, objcategory)
        apiResponseCall.enqueue(object : Callback<AddProductCategoryResponse> {
            override fun onResponse(
                call: Call<AddProductCategoryResponse>,
                response: Response<AddProductCategoryResponse>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: AddProductCategoryResponse? = response.body()
                    if (apiResponse != null) {
                        productDetailView.onProductCategoryListSuccess(apiResponse)
                    } else {
                        val apiResponse: AddProductCategoryResponse = response.body()!!
                        productDetailView.onProductCategoryListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if (apiResponse != null) {
                        productDetailView.onProductCategoryListFailed(apiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<AddProductCategoryResponse>, t: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                productDetailView.onProductCategoryListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }


    fun subCategoryList(
        strCollection: String,
        strValue: String,
        strToken: String,
        strParentCategory: String
    ) {
        val retrofitClient = RetrofitClient(EndPoint.baseUrl1)
        val objsubcategory = JsonObject()
        objsubcategory.addProperty("strCollection", strCollection)
        objsubcategory.addProperty("strValue", strValue)
        val objMainCategory = JsonObject()
        objMainCategory.addProperty("strParentCategory", strParentCategory)
        objsubcategory.add("objCondition", objMainCategory)
        val apiResponseCall: Call<AddProductCategoryResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.addProductCategoryList(strToken, objsubcategory)
        apiResponseCall.enqueue(object : Callback<AddProductCategoryResponse> {
            override fun onResponse(
                call: Call<AddProductCategoryResponse>,
                response: Response<AddProductCategoryResponse>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: AddProductCategoryResponse? = response.body()
                    if (apiResponse != null) {
                        productDetailView.onProductCategoryListSuccess(apiResponse)
                    } else {
                        val apiResponse: AddProductCategoryResponse = response.body()!!
                        productDetailView.onProductCategoryListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if (apiResponse != null) {
                        productDetailView.onProductCategoryListFailed(apiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<AddProductCategoryResponse>, t: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                productDetailView.onProductCategoryListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }


    fun addSizeColor(arrData: ArrayList<String>, intLimit: Int) {
        val retrofitClient = RetrofitClient(EndPoint.baseUrl1)
        val objcategory = JsonObject()
        val arrCategoryList = JsonArray()
        for (String in arrData) {
            val objCollection = JsonObject()
            objCollection.addProperty("strCollection", String)
            objCollection.addProperty("intLimit", intLimit)
            arrCategoryList.add(objCollection)
        }
        objcategory.add("arrCollection", arrCategoryList)
        val apiResponseCall: Call<AddSizeColorResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.addSizeColor(objcategory)
        apiResponseCall.enqueue(object : Callback<AddSizeColorResponse> {
            override fun onResponse(
                call: Call<AddSizeColorResponse>,
                response: Response<AddSizeColorResponse>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: AddSizeColorResponse? = response.body()
                    if (apiResponse != null) {
                        productDetailView.onAddSizeColorSuccess(apiResponse)
                    } else {
                        val apiResponse: AddSizeColorResponse = response.body()!!
                        productDetailView.onAddSizeColorNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if (apiResponse != null) {
                        productDetailView.onAddSizeColorFailed(apiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<AddSizeColorResponse>, t: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                productDetailView.onAddSizeColorFailedServerError(context.getString(R.string.server_error))
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