package tech.stacka.carrymarkdashboard.activity.distributer.addDistributer

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
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.PincodeResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient


class AddDistributerPresenter(private val addDistributerView: AddDistributerView,
                              private val context: Context) {

    fun addDistributer(strToken:String,strName:String,strMobile:String,strEmail:String,strPassword:String,
                       strAddress:String,strExecutiveId:String,strPincode:String,strDist:String,strState:String,
                       strWhatAppNo:String,dblDiscount:String,strGst:String) {
        val objAddEmployee= JsonObject()
        val arrAddress=JsonArray()
        var objAddress=JsonObject()
        objAddress.addProperty("strAddress",strAddress)
        arrAddress.add(objAddress)
        objAddEmployee.addProperty("strName",strName)
        objAddEmployee.addProperty("strMobileNo",strMobile)
        objAddEmployee.addProperty("strEmail",strEmail)
        objAddEmployee.addProperty("strPassword",strPassword)
        objAddEmployee.addProperty("strAddress1",strAddress)
        objAddEmployee.addProperty("strPinCode",strPincode)
        objAddEmployee.addProperty("strDistrict",strDist)
        objAddEmployee.addProperty("strState",strState)
        objAddEmployee.addProperty("dblDiscount",dblDiscount)
        objAddEmployee.addProperty("strWhatsAppNumber",strWhatAppNo)
        objAddEmployee.add("arrAddress",arrAddress)
        objAddEmployee.addProperty("strType","DISTRIBUTER")
        objAddEmployee.addProperty("strExecutiveId",strExecutiveId)
        objAddEmployee.addProperty("strGSTNo",strGst)

        val retrofitClient= RetrofitClient(EndPoint.baseUrl1)
        val apiResponseCall: Call<DefaultResponse> = retrofitClient.instance.createUser(strToken,objAddEmployee)
        apiResponseCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    val apiResponse: DefaultResponse? = response.body()
                    if (apiResponse != null) {
                        addDistributerView.onaddDistributerSuccess(apiResponse)
                    } else {
                        val apiResponse: DefaultResponse = response.body()!!
                        addDistributerView.onaddDistributerNull(apiResponse)
                    }
                } else {
                    var jsonObject: JSONObject? = null
                    jsonObject = JSONObject(response.errorBody()!!.string())
                    Log.e("ErrorBody", jsonObject.toString())
                    val arrErrorCommon = jsonObject.getJSONArray("arrErrors")
                    addDistributerView.onaddDistributerFailed(arrErrorCommon)
//                    val apiResponse: ResponseBody = response.errorBody()!!
//                    addDistributerView.onaddDistributerFailed(apiResponse)
//                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
               // Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                addDistributerView.onaddDistributerFailedServerError(t.toString())
            }
        })
    }

    fun employeeList(strToken:String) {
        val objEmployeeList=JsonObject()
        objEmployeeList.addProperty("strType","EMPLOYEE")
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<EmployeeListResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.employeeList(strToken,objEmployeeList)
        apiResponseCall.enqueue(object : Callback<EmployeeListResponse> {
            override fun onResponse(call: Call<EmployeeListResponse>, response: Response<EmployeeListResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: EmployeeListResponse? = response.body()
                    if (apiResponse != null) {
                        addDistributerView.onEmployeeListSuccess(apiResponse)
                    }else{
                        val apiResponse: EmployeeListResponse = response.body()!!
                        addDistributerView.onEmployeeListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        addDistributerView.onEmployeeListFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<EmployeeListResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                addDistributerView.onEmployeeListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }

    fun stateDistList(strPincode: String) {
        val objstateDistList = JsonObject()
        val arrStateDistList=JsonArray()
        val objCollection=JsonObject()
        objCollection.addProperty("strCollection","cln_post_pin_codes")
        val objPincodeData=JsonObject()
        objPincodeData.addProperty("strName",strPincode)
        objCollection.add("objCondition",objPincodeData)
        val retrofitClient = RetrofitClient(EndPoint.baseUrl1)
        arrStateDistList.add(objCollection)
        objstateDistList.add("arrCollection",arrStateDistList)
        val apiResponseCall: Call<PincodeResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.pincodeList(objstateDistList)
        apiResponseCall.enqueue(object : Callback<PincodeResponse> {
            override fun onResponse(
                call: Call<PincodeResponse>,
                response: Response<PincodeResponse>
            ) {
                if (response.isSuccessful) {
                    val apiResponse: PincodeResponse? = response.body()
                    if (apiResponse != null) {
                        addDistributerView.onStateDistListSuccess(apiResponse)
                    } else {
                        val apiResponse: PincodeResponse = response.body()!!
                        addDistributerView.onStateDistListNull(apiResponse)
                    }
                } else {
                    var jsonObject: JSONObject? = null
                    jsonObject = JSONObject(response.errorBody()!!.string())
                    Log.e("ErrorBody",jsonObject.toString())
                    val arrErrorCommon = jsonObject.getJSONArray("arrErrors")
                    if (arrErrorCommon != null) {
                        addDistributerView.onStateDistListFailed(arrErrorCommon)
                    }
                }
            }

            override fun onFailure(call: Call<PincodeResponse>, t: Throwable) {
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                addDistributerView.onStateDistListFailedServerError(context.getString(R.string.server_error))
            }
        })
    }
}