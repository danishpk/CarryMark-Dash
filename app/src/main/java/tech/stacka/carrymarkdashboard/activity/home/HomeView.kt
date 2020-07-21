package tech.stacka.carrymarkdashboard.activity.home

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.ReportResponse

interface HomeView {
    abstract fun onReportSuccess(apiResponse: ReportResponse)
    abstract fun onReportNull(apiResponse: ReportResponse)
    abstract fun onReportFailed(apiResponse: ResponseBody)
    abstract fun onReportServerError(string: String)
//    abstract fun onHomeDataSuccess(apiResponse: ProductResponse)
//    abstract fun onHomeDataFailed(apiResponse: ProductResponse)
//    abstract fun onHomeDataFailedServerError(string: String)
}