package tech.stacka.carrymarkdashboard.activity.map

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.EmployeeLocationResponse

interface MapView {
    abstract fun onEmployeeLocationSuccess(apiResponse: EmployeeLocationResponse)
    abstract fun onEmployeeLocationNull(apiResponse: EmployeeLocationResponse)
    abstract fun onEmployeeLocationFailed(apiResponse: ResponseBody)
    abstract fun onEmployeeLocationFailedServerError(string: String)
}