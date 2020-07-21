package tech.stacka.carrymarkdashboard.activity.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.LoginResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class LoginPresenter(loginView: LoginView, mContext: Context) {
    private val loginView: LoginView = loginView
    private val mContext: Context = mContext
    fun loginUserPass(strName: String?, strPassword: String?,strFirebaseToken:String) {
                val objUserLogin= JsonObject()
        objUserLogin.addProperty("strName",strName)
        objUserLogin.addProperty("strType","ADMIN")
        objUserLogin.addProperty("strPassword",strPassword)
        objUserLogin.addProperty("strFirbaseToken",strFirebaseToken)
        val retrofitClient = RetrofitClient(EndPoint.baseUrl1)
        val apiResponseCall: Call<LoginResponse> =
           // RetrofitClient.instance.authUserLogin(objUserLogin)
        retrofitClient.instance.authUserLogin(objUserLogin)
        apiResponseCall.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful) {
                    val apiResponse: LoginResponse? = response.body()
                    if (apiResponse != null) {
                        loginView.onLoginSuccess(apiResponse)
                    }else{
                        val apiResponse: LoginResponse = response.body()!!
                        loginView.onLoginNull(apiResponse)
                    }
                } else {
                    var jsonObject: JSONObject? = null
                    jsonObject = JSONObject(response.errorBody()!!.string())
                    Log.e("ErrorBody",jsonObject.toString())
                    val arrErrorCommon = jsonObject.getJSONArray("arrErrors")
                    loginView.onLoginFailed(arrErrorCommon)
//                    val apiResponse: ResponseBody = response.errorBody()!!
//                    if(apiResponse!=null) {
//                        loginView.onLoginFailed(apiResponse)
//                    }
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
         //       Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show()
                loginView.onLoginFailedServerError(mContext.getString(R.string.server_error))
            }
        })
    }
}