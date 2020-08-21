package tech.stacka.carrymarkdashboard.activity.map

import org.json.JSONArray
import tech.stacka.carrymarkdashboard.models.EmployeeLocationResponse

interface MapView {
    abstract fun onEmployeeLocationSuccess(apiResponse: EmployeeLocationResponse)
    abstract fun onEmployeeLocationNull(apiResponse: EmployeeLocationResponse)
    abstract fun onEmployeeLocationFailed(apiResponse: JSONArray)
    abstract fun onEmployeeLocationFailedServerError(string: String)
}