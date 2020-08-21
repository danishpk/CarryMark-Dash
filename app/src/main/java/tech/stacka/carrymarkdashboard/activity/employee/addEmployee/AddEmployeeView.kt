package tech.stacka.carrymarkdashboard.activity.employee.addEmployee

import org.json.JSONArray
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.PincodeResponse

interface AddEmployeeView {
    abstract fun onaddEmployeeSuccess(apiResponse: DefaultResponse)
    abstract fun onaddEmployeeNull(apiResponse: DefaultResponse)
    abstract fun onaddEmployeeFailed(apiResponse: JSONArray)
    abstract fun onaddEmployeeFailedServerError(string: String)
    abstract fun onStateDistListSuccess(apiResponse: PincodeResponse)
    abstract fun onStateDistListNull(apiResponse: PincodeResponse)
    abstract fun onStateDistListFailed(arrErrorCommon: JSONArray)
    abstract fun onStateDistListFailedServerError(string: String)
}