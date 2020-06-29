package tech.stacka.carrymarkdashboard.activity.distributer.addDistributer

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.DefaultResponse

interface AddDistributerView {
    abstract fun onaddDistributerSuccess(apiResponse: DefaultResponse)
    abstract fun onaddDistributerNull(apiResponse: DefaultResponse)
    abstract fun onaddDistributerFailed(apiResponse: ResponseBody)
    abstract fun onaddDistributerFailedServerError(string: String)
}