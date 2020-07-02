package tech.stacka.carrymarkdashboard.activity.retailer.retailerList

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.DistributerListResponse
import tech.stacka.carrymarkdashboard.models.EmployeeListResponse
import tech.stacka.carrymarkdashboard.models.RetailerListResponse

interface RetailerListView {
    abstract fun onRetailerListSuccess(apiResponse: RetailerListResponse)
    abstract fun onRetailerListFailed(apiResponse: ResponseBody)
    abstract fun onRetailerListFailedServerError(string: String)
    abstract fun onRetailerListNull(apiResponse: RetailerListResponse)
    abstract fun onEmployeeListSuccess(apiResponse: EmployeeListResponse)
    abstract fun onEmployeeListNull(apiResponse: EmployeeListResponse)
    abstract fun onEmployeeListFailed(apiResponse: ResponseBody)
    abstract fun onEmployeeListFailedServerError(string: String)
    abstract fun onDistributerListNull(apiResponse: DistributerListResponse)
    abstract fun onDistributerListSuccess(apiResponse: DistributerListResponse)
    abstract fun onDistributerListFailed(apiResponse: ResponseBody)
    abstract fun onDistributerListFailedServerError(string: String)
}