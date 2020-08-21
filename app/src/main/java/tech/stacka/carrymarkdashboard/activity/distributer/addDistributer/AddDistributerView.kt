package tech.stacka.carrymarkdashboard.activity.distributer.addDistributer

import okhttp3.ResponseBody
import org.json.JSONArray
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.PincodeResponse

interface AddDistributerView {
    abstract fun onaddDistributerSuccess(apiResponse: DefaultResponse)
    abstract fun onaddDistributerNull(apiResponse: DefaultResponse)
    abstract fun onaddDistributerFailedServerError(string: String)
    abstract fun onEmployeeListSuccess(apiResponse: EmployeeListResponse)
    abstract fun onEmployeeListNull(apiResponse: EmployeeListResponse)
    abstract fun onEmployeeListFailed(apiResponse: ResponseBody)
    abstract fun onEmployeeListFailedServerError(string: String)
    abstract fun onaddDistributerFailed(arrErrorCommon: JSONArray)
    abstract fun onStateDistListSuccess(apiResponse: PincodeResponse)
    abstract fun onStateDistListNull(apiResponse: PincodeResponse)
    abstract fun onStateDistListFailed(arrErrorCommon: JSONArray)
    abstract fun onStateDistListFailedServerError(string: String)
}