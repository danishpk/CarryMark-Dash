package tech.stacka.carrymarkdashboard.activity.home

import org.json.JSONArray
import tech.stacka.carrymarkdashboard.models.ReportResponse

interface HomeView {
    abstract fun onReportSuccess(apiResponse: ReportResponse)
    abstract fun onReportNull(apiResponse: ReportResponse)
    abstract fun onReportFailed(apiResponse: JSONArray)
    abstract fun onReportServerError(string: String)
//    abstract fun onHomeDataSuccess(apiResponse: ProductResponse)
//    abstract fun onHomeDataFailed(apiResponse: ProductResponse)
//    abstract fun onHomeDataFailedServerError(string: String)
}