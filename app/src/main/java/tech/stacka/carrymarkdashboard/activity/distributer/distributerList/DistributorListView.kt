package tech.stacka.carrymarkdashboard.activity.distributer.distributerList

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.DistributerListResponse

interface DistributorListView {
    abstract fun onDistributerListSuccess(apiResponse: DistributerListResponse)
    abstract fun onDistributerListFailed(apiResponse: ResponseBody)
    abstract fun onDistributerListFailedServerError(string: String)
    abstract fun onDistributerListNull(apiResponse: DistributerListResponse)
}