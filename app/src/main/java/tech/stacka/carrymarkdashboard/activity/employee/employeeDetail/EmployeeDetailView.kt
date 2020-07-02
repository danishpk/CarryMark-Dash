package tech.stacka.carrymarkdashboard.activity.employee.employeeDetail

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.EmployeeDetailResponse

interface EmployeeDetailView {
    abstract fun onEmployeeDetailSuccess(apiResponse: EmployeeDetailResponse)
    abstract fun onEmployeeDetailNull(apiResponse: EmployeeDetailResponse)
    abstract fun onEmployeeDetailFailed(apiResponse: ResponseBody)
    abstract fun onEmployeeDetailFailedServerError(string: String)
}