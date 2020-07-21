package tech.stacka.carrymarkdashboard.activity.employee.addEmployee

import org.json.JSONArray
import tech.stacka.carrymarkdashboard.models.DefaultResponse

interface AddEmployeeView {
    abstract fun onaddEmployeeSuccess(apiResponse: DefaultResponse)
    abstract fun onaddEmployeeNull(apiResponse: DefaultResponse)
    abstract fun onaddEmployeeFailed(apiResponse: JSONArray)
    abstract fun onaddEmployeeFailedServerError(string: String)
}