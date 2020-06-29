package tech.stacka.carrymarkdashboard.activity.retailer.retailerList

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.RetailerListResponse

interface RetailerListView {
    abstract fun onRetailerListSuccess(apiResponse: RetailerListResponse)
    abstract fun onRetailerListFailed(apiResponse: ResponseBody)
    abstract fun onRetailerListFailedServerError(string: String)
    abstract fun onRetailerListNull(apiResponse: RetailerListResponse)
}