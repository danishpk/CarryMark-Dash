package tech.stacka.carrymarkdashboard.activity.employee.employeeList

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse

interface EmployeeListView {
    abstract fun onEmployeeListSuccess(apiResponse: EmployeeListResponse)
    abstract fun onEmployeeListFailed(apiResponse: ResponseBody)
    abstract fun onEmployeeListFailedServerError(string: String)
    abstract fun onEmployeeListNull(apiResponse: EmployeeListResponse)
}