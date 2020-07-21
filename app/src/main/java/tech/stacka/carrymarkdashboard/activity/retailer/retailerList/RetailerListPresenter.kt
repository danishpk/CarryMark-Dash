package tech.stacka.carrymarkdashboard.activity.retailer.retailerList

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.DistributerListResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.RetailerListResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class RetailerListPresenter(retailreListView: RetailerListView, context: Context) {

    private val retailerListView: RetailerListView = retailreListView
    private val context: Context = context


    fun retailerList(strToken: String) {
        val objRetailerList = JsonObject()
        objRetailerList.addProperty("strType", "RETAILER")
        val retrofitClient = RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<RetailerListResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.retailerList(strToken, objRetailerList)
        apiResponseCall.enqueue(object : Callback<RetailerListResponse> {
            override fun onResponse(
                call: Call<RetailerListResponse>,
                response: Response<RetailerListResponse>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: RetailerListResponse? = response.body()
                    if (apiResponse != null) {
                        retailerListView.onRetailerListSuccess(apiResponse)
                    } else {
                        val apiResponse: RetailerListResponse = response.body()!!
                        retailerListView.onRetailerListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    retailerListView.onRetailerListFailed(apiResponse)
                }
            }

            override fun onFailure(call: Call<RetailerListResponse>, t: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                retailerListView.onRetailerListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }


    fun employeeList(strToken: String) {
        val objEmployeeList = JsonObject()
        objEmployeeList.addProperty("strType", "EMPLOYEE")
        val retrofitClient = RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<EmployeeListResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.employeeList(strToken, objEmployeeList)
        apiResponseCall.enqueue(object : Callback<EmployeeListResponse> {
            override fun onResponse(
                call: Call<EmployeeListResponse>,
                response: Response<EmployeeListResponse>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: EmployeeListResponse? = response.body()
                    if (apiResponse != null) {
                        retailerListView.onEmployeeListSuccess(apiResponse)
                    } else {
                        val apiResponse: EmployeeListResponse = response.body()!!
                        retailerListView.onEmployeeListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if (apiResponse != null) {
                        retailerListView.onEmployeeListFailed(apiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<EmployeeListResponse>, t: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                retailerListView.onEmployeeListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }

    fun distributerList(strToken: String) {
        val objDistributerList = JsonObject()
        objDistributerList.addProperty("strType", "DISTRIBUTER")
        val retrofitClient = RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<DistributerListResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.distributerList(strToken, objDistributerList)
        apiResponseCall.enqueue(object : Callback<DistributerListResponse> {
            override fun onResponse(
                call: Call<DistributerListResponse>,
                response: Response<DistributerListResponse>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: DistributerListResponse? = response.body()
                    if (apiResponse != null) {
                        retailerListView.onDistributerListSuccess(apiResponse)
                    } else {
                        val apiResponse: DistributerListResponse = response.body()!!
                        retailerListView.onDistributerListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if (apiResponse != null) {
                        retailerListView.onDistributerListFailed(apiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<DistributerListResponse>, t: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                retailerListView.onDistributerListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }

    fun updateRetailer( strToken: String,
                        strExecutiveId: String,
                       strExecutiveName: String,
                       strDistributorId: String,
                       strDistributorName: String,
                       strActive: String,
                       strRetailerId:String) {


        val objUpdateRetailer = JsonObject()
        objUpdateRetailer.addProperty("strDistributerId", strDistributorId)
        objUpdateRetailer.addProperty("strDistributerName",strDistributorName)
        objUpdateRetailer.addProperty("strActiveStatus",strActive)
        objUpdateRetailer.addProperty("strExecutiveId",strExecutiveId)
        objUpdateRetailer.addProperty("strExecutiveName",strExecutiveName)
        objUpdateRetailer.addProperty("strUserDocId",strRetailerId)
        val retrofitClient = RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<DefaultResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.updateRetailer(strToken, objUpdateRetailer)
        apiResponseCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful()) {
                    val apiResponse: DefaultResponse? = response.body()
                    if (apiResponse != null) {
                        retailerListView.onRetailerUpdateSuccess(apiResponse)
                    } else {
                        val apiResponse: DefaultResponse = response.body()!!
                        retailerListView.onRetailerUpdateNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if (apiResponse != null) {
                        retailerListView.onRetailerUpdateFailed(apiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                retailerListView.onRetailerUpdateFailedServerError(context.getString(R.string.server_error))
            }
        })
    }
}