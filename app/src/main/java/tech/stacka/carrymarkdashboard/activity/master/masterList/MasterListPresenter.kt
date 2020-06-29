package tech.stacka.carrymarkdashboard.activity.master.masterList

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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


}