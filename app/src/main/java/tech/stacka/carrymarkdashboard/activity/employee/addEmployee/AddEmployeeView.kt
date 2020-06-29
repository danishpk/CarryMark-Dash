package tech.stacka.carrymarkdashboard.activity.employee.addEmployee

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.DefaultResponse

interface AddEmployeeView {
    abstract fun onaddEmployeeSuccess(apiResponse: DefaultResponse)
    abstract fun onaddEmployeeNull(apiResponse: DefaultResponse)
    abstract fun onaddEmployeeFailed(apiResponse: ResponseBody)
    abstract fun onaddEmployeeFailedServerError(string: String)
}