package tech.stacka.carrymarkdashboard.network

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import tech.stacka.carrymarkdashboard.models.*


interface ServiceInterface {

    @POST(EndPoint.userLogin)
    @Headers("Content-Type: application/json",EndPoint.strAppInfo)
    fun authUserLogin(@Body userlogin: JsonObject): Call<LoginResponse>

    @POST(EndPoint.productList)
    @Headers("Content-Type: application/json",EndPoint.strAppInfo)
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
    @Headers("Content-Type: application/json",EndPoint.strAppInfo)
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

    @POST(EndPoint.userList)
    @Headers("Content-Type: application/json")
    fun adminList(@Header("Authorization")strToken:String,
                        @Body userList: JsonObject): Call<AdminResponse>

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
    @Headers("Content-Type: application/json",EndPoint.strAppInfo)
    fun addSizeColor(@Body addSizeColor: JsonObject): Call<AddSizeColorResponse>

    @POST(EndPoint.getMaster)
    @Headers("Content-Type: application/json",EndPoint.strAppInfo)
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

    @POST(EndPoint.orderList)
    @Headers("Content-Type: application/json")
    fun orderList(@Header("Authorization")strToken:String,
                  @Body orderList: JsonObject): Call<OrderListResponse>

    @POST(EndPoint.orderDetail)
    @Headers("Content-Type: application/json")
    fun orderDetail(@Header("Authorization")strToken:String,
                    @Body orderDetail: JsonObject): Call<OrderDetailResponse>

    @POST(EndPoint.orderUpdate)
    @Headers("Content-Type: application/json")
    fun orderUpdate(@Header("Authorization")strToken:String,
                    @Body orderDetail: JsonObject): Call<DefaultResponse>

    @POST(EndPoint.updateRetailer)
    @Headers("Content-Type: application/json")
    fun updateRetailer(@Header("Authorization")strToken:String,
                    @Body updateRetailer: JsonObject): Call<DefaultResponse>

    @POST(EndPoint.getReports)
    @Headers("Content-Type: application/json")
    fun getReport(@Header("Authorization")strToken:String,
                       @Body getReport: JsonObject): Call<ReportResponse>

    @POST(EndPoint.getEmpLocations)
    @Headers("Content-Type: application/json")
    fun getLocation(@Header("Authorization")strToken:String,
                  @Body getLocation: JsonObject): Call<EmployeeLocationResponse>

    @POST(EndPoint.createTarget)
    @Headers("Content-Type: application/json")
    fun createTarget(@Header("Authorization")strToken:String,
                    @Body createTarget: JsonObject): Call<DefaultResponse>

    @POST(EndPoint.notificationList)
    @Headers("Content-Type: application/json")
    fun notificationList(@Header("Authorization")strToken:String): Call<NotificationListResponse>

    @POST(EndPoint.createNotification)
    @Headers("Content-Type: application/json")
    fun createNotification(@Header("Authorization")strToken:String,
                     @Body createNotification: JsonObject): Call<DefaultResponse>

    @POST(EndPoint.deleteUser)
    @Headers("Content-Type: application/json")
    fun deleteUser(@Header("Authorization")strToken:String,
                      @Body deleteUser: JsonObject): Call<DefaultResponse>

    @POST(EndPoint.userList)
    @Headers("Content-Type: application/json")
    fun distributorDetail(@Header("Authorization")strToken:String,
                       @Body distributorDetail: JsonObject): Call<DistributorDetailResponse>

    @POST(EndPoint.getMaster)
    @Headers("Content-Type: application/json",EndPoint.strAppInfo)
    fun pincodeList(@Body filterCategoryList: JsonObject): Call<PincodeResponse>

    @POST(EndPoint.getReports)
    @Headers("Content-Type: application/json")
    fun getTargetHistory(@Header("Authorization")strToken:String,
                  @Body getReport: JsonObject): Call<TargetHistoryResponse>

    @POST(EndPoint.userList)
    @Headers("Content-Type: application/json")
    fun retailerDetail(@Header("Authorization")strToken:String,
                       @Body userDetail: JsonObject): Call<RetailerDetailResponse>


}

