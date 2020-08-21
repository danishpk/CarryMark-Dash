package tech.stacka.carrymarkdashboard.activity.employee.employeeDetail

import okhttp3.ResponseBody
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.EmployeeDetailResponse
import tech.stacka.carrymarkdashboard.models.ReportResponse

interface EmployeeDetailView {
    abstract fun onEmployeeDetailSuccess(apiResponse: EmployeeDetailResponse)
    abstract fun onEmployeeDetailNull(apiResponse: EmployeeDetailResponse)
    abstract fun onEmployeeDetailFailed(apiResponse: ResponseBody)
    abstract fun onEmployeeDetailFailedServerError(string: String)
    abstract fun onEmployeeTargetSuccess(apiResponse: DefaultResponse)
    abstract fun onEmployeeTargetNull(apiResponse: DefaultResponse)
    abstract fun onEmployeeTargetFailed(apiResponse: ResponseBody)
    abstract fun onEmployeeTargetFailedServerError(string: String)
    abstract fun onReportSuccess(apiResponse: ReportResponse)
    abstract fun onReportNull(apiResponse: ReportResponse)
    abstract fun onReportFailed(apiResponse: ResponseBody)
    abstract fun onReportServerError(string: String)
    abstract fun onDeleteUserSuccess(apiResponse: DefaultResponse)
    abstract fun onDeleteUserNull(apiResponse: DefaultResponse)
    abstract fun onDeleteUserFailed(apiResponse: JSONArray)
    abstract fun onDeleteUserServerError(string: String)
}