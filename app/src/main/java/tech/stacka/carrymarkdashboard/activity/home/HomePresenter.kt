package tech.stacka.carrymarkdashboard.activity.home

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.widget.Toast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.ReportResponse
import tech.stacka.carrymarkdashboard.models.UploadProductImageResponse
import tech.stacka.carrymarkdashboard.network.EndPoint
import tech.stacka.carrymarkdashboard.network.RetrofitClient
import java.io.File

class HomePresenter(private val homeView: HomeView, private val mContext: Context) {

    fun getReport(strToken: String) {
        val objReport = JsonObject()
        var arrReport= JsonArray()
        var objReportData1= JsonObject()
        var objReportData2= JsonObject()
        objReportData1.addProperty("strName","SALE")
        objReportData2.addProperty("strName","ORDER")
        arrReport.add(objReportData1)
        arrReport.add(objReportData2)
        objReport.add("arrReport",arrReport)

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
                        homeView.onReportSuccess(apiResponse)
                    } else {
                        val apiResponse: ReportResponse = response.body()!!
                        homeView.onReportNull(apiResponse)
                    }
                } else {
                    val apiResponse: ResponseBody = response.errorBody()!!
                    if (apiResponse != null) {
                        homeView.onReportFailed(apiResponse)
                    }
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                Toast.makeText(mContext, "error", Toast.LENGTH_LONG).show()
                homeView.onReportServerError(mContext.getString(R.string.server_error))
            }
        })
    }


}