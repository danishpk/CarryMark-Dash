package tech.stacka.carrymarkdashboard.activity.retailer.retailerList

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.DefaultResponse
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
    abstract fun onRetailerUpdateSuccess(apiResponse: DefaultResponse)
    abstract fun onRetailerUpdateNull(apiResponse: DefaultResponse)
    abstract fun onRetailerUpdateFailed(apiResponse: ResponseBody)
    abstract fun onRetailerUpdateFailedServerError(string: String)
    abstract fun onRetailerSearchListSuccess(apiResponse: RetailerListResponse)
    abstract fun onRetailerSearchListNull(apiResponse: RetailerListResponse)
    abstract fun onRetailerSearchListFailed(apiResponse: ResponseBody)
    abstract fun onRetailerListSearchFailedServerError(string: String)
    abstract fun onRetailerListMoreSuccess(apiResponse: RetailerListResponse)
    abstract fun onRetailerListMOreNull(apiResponse: RetailerListResponse)
    abstract fun onRetailerListMoreFailed(apiResponse: ResponseBody)
    abstract fun onRetailerListFailedMoreServerError(string: String)
}