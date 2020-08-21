package tech.stacka.carrymarkdashboard.activity.employee.employeeDetail

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.EmployeeDetailResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.ReportResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class EmployeeDetailPresenter(val employeeDetailView: EmployeeDetailView,val context: Context) {


    fun employeeDetails(strToken:String,strEmpoyeeId:String) {
        val objEmployeeList= JsonObject()
        objEmployeeList.addProperty("strType","EMPLOYEE")
        objEmployeeList.addProperty("strDocId",strEmpoyeeId)
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<EmployeeDetailResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.employeeDetail(strToken,objEmployeeList)
        apiResponseCall.enqueue(object : Callback<EmployeeDetailResponse> {
            override fun onResponse(call: Call<EmployeeDetailResponse>, response: Response<EmployeeDetailResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: EmployeeDetailResponse? = response.body()
                    if (apiResponse != null) {
                        employeeDetailView.onEmployeeDetailSuccess(apiResponse)
                    }else{
                        val apiResponse: EmployeeDetailResponse = response.body()!!
                        employeeDetailView.onEmployeeDetailNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        employeeDetailView.onEmployeeDetailFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<EmployeeDetailResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                employeeDetailView.onEmployeeDetailFailedServerError(context.getString(R.string.server_error))
            }
        })
    }


    fun createTarget(strToken:String,strExecutiveId:String,dblSaleTarget:Int) {
        val objEmployeeTarget= JsonObject()
        objEmployeeTarget.addProperty("strTargetType","SALE")
        objEmployeeTarget.addProperty("strExecutiveId",strExecutiveId)
        objEmployeeTarget.addProperty("dblSaleTarget",dblSaleTarget)
        Log.i("CreateTarget",objEmployeeTarget.toString())
        val retrofitClient= RetrofitClient(EndPoint.baseUrl3)
        val apiResponseCall: Call<DefaultResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.createTarget(strToken,objEmployeeTarget)
        apiResponseCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    val apiResponse: DefaultResponse? = response.body()
                    if (apiResponse != null) {
                        employeeDetailView.onEmployeeTargetSuccess(apiResponse)
                    }else{
                        val apiResponse: DefaultResponse = response.body()!!
                        employeeDetailView.onEmployeeTargetNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        employeeDetailView.onEmployeeTargetFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                employeeDetailView.onEmployeeTargetFailedServerError(context.getString(R.string.server_error))
            }
        })
    }

    fun getReport(strToken: String,strEmployeeId:String) {
        val objReport = JsonObject()
        var arrReport= JsonArray()
        var objReportData1= JsonObject()
        objReportData1.addProperty("strName","SALE")
        arrReport.add(objReportData1)
        objReport.add("arrReport",arrReport)
        objReport.addProperty("strExecutiveId",strEmployeeId)
        Log.i("EmployeeReport",objReport.toString())
        val retrofitClient = RetrofitClient(EndPoint.baseUrl3)
        val apiResponseCall: Call<ReportResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.getReport(strToken, objReport)
        apiResponseCall.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse: ReportResponse? = response.body()
                    if (apiResponse != null) {
                        employeeDetailView.onReportSuccess(apiResponse)
                    } else {
                        val apiResponse: ReportResponse = response.body()!!
                        employeeDetailView.onReportNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if (apiResponse != null) {
                        employeeDetailView.onReportFailed(apiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
              //  Toast.makeText(mContext, "error", Toast.LENGTH_LONG).show()
                employeeDetailView.onReportServerError(context.getString(R.string.server_error))
            }
        })
    }


    fun deleteUser(strToken: String,strUserId:String) {
        val objUserId = JsonObject()
        var arrUserIds= JsonArray()
        arrUserIds.add(strUserId)
        objUserId.add("arrDeleteId",arrUserIds)

        val retrofitClient = RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<DefaultResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.deleteUser(strToken, objUserId)
        apiResponseCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse: DefaultResponse? = response.body()
                    if (apiResponse != null) {
                        employeeDetailView.onDeleteUserSuccess(apiResponse)
                    } else {
                        val apiResponse: DefaultResponse = response.body()!!
                        employeeDetailView.onDeleteUserNull(apiResponse)
                    }
                } else {
                   // val apiResponse: ResponseBody = response.errorBody()!!

                    var jsonObject: JSONObject? = null
                    jsonObject = JSONObject(response.errorBody()!!.string())
                    Log.e("ErrorBody",jsonObject.toString())
                    val arrErrorCommon = jsonObject.getJSONArray("arrErrors")
                    if (arrErrorCommon != null) {
                        employeeDetailView.onDeleteUserFailed(arrErrorCommon)
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                //  Toast.makeText(mContext, "error", Toast.LENGTH_LONG).show()
                employeeDetailView.onDeleteUserServerError(context.getString(R.string.server_error))
            }
        })
    }

}