package tech.stacka.carrymarkdashboard.activity.master.masterList

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.GetMasterResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient

class MasterListPresenter(val masterListView: MasterListView,val context: Context) {

    fun getMaster(arrCategory: ArrayList<String>, intLimit:Int) {
        val retrofitClient= RetrofitClient(EndPoint.baseUrl1)
        val objmaster= JsonObject()
        val arrCategoryList= JsonArray()

        for(String in arrCategory){
            val objCollection= JsonObject()
            objCollection.addProperty("strCollection",String)
            objCollection.addProperty("intLimit",intLimit)
            arrCategoryList.add(objCollection)
        }
        objmaster.add("arrCollection",arrCategoryList)
        val apiResponseCall: Call<GetMasterResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.getMaster(objmaster)
        apiResponseCall.enqueue(object : Callback<GetMasterResponse> {
            override fun onResponse(call: Call<GetMasterResponse>, response: Response<GetMasterResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: GetMasterResponse? = response.body()
                    if (apiResponse != null) {
                        masterListView.onMasterListSuccess(apiResponse)
                    }else{
                        val apiResponse: GetMasterResponse = response.body()!!
                        masterListView.onMasterListNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    masterListView.onMasterListFailed(apiResponse)
                }
            }
            override fun onFailure(call: Call<GetMasterResponse>, t: Throwable) {
                //    Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                masterListView.onMasterServerFailed(t.toString())
            }
        })
    }

    fun deleteMaster(strCollection:String,strId:String,strToken:String) {
        val objDelMaster= JsonObject()
        val arrCategoryId= JsonArray()
        arrCategoryId.add(strId)
        objDelMaster.addProperty("strCollection",strCollection)
        objDelMaster.add("arrDeleteId",arrCategoryId)
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val apiResponseCall: Call<DefaultResponse> =
            //  RetrofitClient.instance.productList()
            retrofitClient.instance.delMaster(strToken,objDelMaster)
        apiResponseCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: DefaultResponse? = response.body()
                    if (apiResponse != null) {
                        masterListView.onDelMasterSuccess(apiResponse)
                    }else{
                        val apiResponse: DefaultResponse = response.body()!!
                        masterListView.onDelMasterNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    masterListView.onDelMasterFailed(apiResponse)
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                //    Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                masterListView.onDelMasterServerFailed(t.toString())
            }
        })
    }


}