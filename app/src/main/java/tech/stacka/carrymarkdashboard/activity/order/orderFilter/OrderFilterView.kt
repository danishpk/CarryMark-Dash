package tech.stacka.carrymarkdashboard.activity.order.orderFilter

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.DistributerListResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse

interface OrderFilterView {
    abstract fun onDistributerListSuccess(apiResponse: DistributerListResponse)
    abstract fun onDistributerListNull(apiResponse: DistributerListResponse)
    abstract fun onDistributerListFailed(apiResponse: ResponseBody)
    abstract fun onDistributerListFailedServerError(string: String)
    abstract fun onEmployeeListSuccess(apiResponse: EmployeeListResponse)
    abstract fun onEmployeeListNull(apiResponse: EmployeeListResponse)
    abstract fun onEmployeeListFailed(apiResponse: ResponseBody)
    abstract fun onEmployeeListFailedServerError(string: String)
}