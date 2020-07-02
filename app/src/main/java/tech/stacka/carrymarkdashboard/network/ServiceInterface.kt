package tech.stacka.carrymarkdashboard.network

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import tech.stacka.carrymarkdashboard.models.*


interface ServiceInterface {

    @POST(EndPoint.userLogin)
    @Headers("Content-Type: application/json","strAppInfo: TNT1")
    fun authUserLogin(@Body userlogin: JsonObject): Call<LoginResponse>

    @POST(EndPoint.productList)
    @Headers("Content-Type: application/json","strAppInfo: TNT1")
    fun productList(@Body productList: JsonObject): Call<ProductListResponse>

    @POST(EndPoint.addProduct)
    @Headers("Content-Type: application/json")
    fun addProduct(@Header("Authorization")strToken:String,@Body addProduct: JsonObject): Call<AddProductResponse>

    @POST(EndPoint.addProductCategoryList)
    @Headers("Content-Type: application/json")
    fun addProductCategoryList(@Header("Authorization")strToken:String,
                               @Body addProductCategoryList: JsonObject): Call<AddProductCategoryResponse>

    @Multipart
    @POST(EndPoint.uploadProductImage)
    fun uploadProductImage(@Header("Authorization")strToken:String,
                           @Part images:List<MultipartBody.Part>):Call<UploadProductImageResponse>

    @POST(EndPoint.getMaster)
    @Headers("Content-Type: application/json","strAppInfo: TNT1")
    fun filterCategoryList(@Body filterCategoryList: JsonObject): Call<FilterCategoryResponse>

    @POST(EndPoint.userList)
    @Headers("Content-Type: application/json")
    fun employeeList(@Header("Authorization")strToken:String,
                     @Body userList: JsonObject): Call<EmployeeListResponse>

    @POST(EndPoint.userList)
    @Headers("Content-Type: application/json")
    fun distributerList(@Header("Authorization")strToken:String,
                        @Body userList: JsonObject): Call<DistributerListResponse>

    @POST(EndPoint.userList)
    @Headers("Content-Type: application/json")
    fun retailerList(@Header("Authorization")strToken:String,
                     @Body userList: JsonObject): Call<RetailerListResponse>

    @POST(EndPoint.createUser)
    @Headers("Content-Type: application/json")
    fun createUser(@Header("Authorization")strToken:String,
                     @Body createUser: JsonObject): Call<DefaultResponse>


    @HTTP(method = "DELETE", path = EndPoint.deleteProduct, hasBody = true)
   // @DELETE(EndPoint.deleteProduct)
    @Headers("Content-Type: application/json")
    fun deleteProduct(@Header("Authorization")strToken:String,
                   @Body createUser: JsonObject): Call<DefaultResponse>

    @POST(EndPoint.getMaster)
    @Headers("Content-Type: application/json","strAppInfo: TNT1")
    fun addSizeColor(@Body addSizeColor: JsonObject): Call<AddSizeColorResponse>

    @POST(EndPoint.getMaster)
    @Headers("Content-Type: application/json","strAppInfo: TNT1")
    fun getMaster(@Body getMaster: JsonObject): Call<GetMasterResponse>

    @POST(EndPoint.createMaster)
    @Headers("Content-Type: application/json")
    fun createMaster(@Header("Authorization")strToken:String,
                     @Body createMaster: JsonObject): Call<DefaultResponse>


    @HTTP(method = "DELETE", path = EndPoint.deleteMaster, hasBody = true)
    @Headers("Content-Type: application/json")
    fun delMaster(@Header("Authorization")strToken:String,
                      @Body delMaster: JsonObject): Call<DefaultResponse>

    @POST(EndPoint.productDetails)
    @Headers("Content-Type: application/json")
    fun productDetail(@Header("Authorization")strToken:String,
                     @Body productDetails: JsonObject): Call<ProductDetailResponse>


    @POST(EndPoint.userList)
    @Headers("Content-Type: application/json")
    fun employeeDetail(@Header("Authorization")strToken:String,
                     @Body employeeDetail: JsonObject): Call<EmployeeDetailResponse>
}

