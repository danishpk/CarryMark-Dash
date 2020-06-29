package tech.stacka.carrymarkdashboard.activity.master.addMaster

import android.content.Context
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.UploadProductImageResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient
import java.io.File

class AddMasterPresenter(val addMasterView: AddMasterView,val context: Context) {

    fun uploadImage(paths:List<File>, strToken:String) {
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
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
                        addMasterView.uploadImageSuccess(apiResponse)
                    }else{
                        val apiResponse: UploadProductImageResponse = response.body()!!
                        addMasterView.uploadImageNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        addMasterView.uploadImageFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<UploadProductImageResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                addMasterView.uploadImageFailedServerError(t.toString()
                    //   mContext.getString(R.string.server_error)
                )
            }
        })
    }

    fun createMaster( strToken:String,strMainCategory:String,strMaster:String,strImageData:String) {
        val retrofitClient= RetrofitClient(EndPoint.baseUrl2)
        val addMasterJson = JsonObject()
        addMasterJson.addProperty("strName",strMaster)
        addMasterJson.addProperty("strCollection",strMainCategory)
        addMasterJson.addProperty("strImgUrl_0",strImageData)
        val apiResponseCall: Call<DefaultResponse> = retrofitClient.instance.createMaster(strToken,addMasterJson)
        apiResponseCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful()) {
                    val apiResponse: DefaultResponse? = response.body()
                    if (apiResponse != null) {
                        addMasterView.addMasterSuccess(apiResponse)
                    }else{
                        val apiResponse: DefaultResponse = response.body()!!
                        addMasterView.addMasterNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if(apiResponse!=null) {
                        addMasterView.addMasterFailed(apiResponse)
                    }
                }
            }
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(context,"error", Toast.LENGTH_LONG).show()
                addMasterView.addMasterFailedServerError(t.toString()
                    //   mContext.getString(R.string.server_error)
                )
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