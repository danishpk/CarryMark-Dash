package tech.stacka.carrymarkdashboard.activity.distributer.addDistributer

import okhttp3.ResponseBody
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse

interface AddDistributerView {
    abstract fun onaddDistributerSuccess(apiResponse: DefaultResponse)
    abstract fun onaddDistributerNull(apiResponse: DefaultResponse)
    abstract fun onaddDistributerFailedServerError(string: String)
    abstract fun onEmployeeListSuccess(apiResponse: EmployeeListResponse)
    abstract fun onEmployeeListNull(apiResponse: EmployeeListResponse)
    abstract fun onEmployeeListFailed(apiResponse: ResponseBody)
    abstract fun onEmployeeListFailedServerError(string: String)
    abstract fun onaddDistributerFailed(arrErrorCommon: JSONArray)
}