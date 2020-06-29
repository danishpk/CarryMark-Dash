package tech.stacka.carrymarkdashboard.activity.master.masterList

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.GetMasterResponse

interface MasterListView {
    abstract fun onMasterListSuccess(apiResponse: GetMasterResponse)
    abstract fun onMasterListNull(apiResponse: GetMasterResponse)
    abstract fun onMasterListFailed(apiResponse: ResponseBody)
    abstract fun onMasterServerFailed(toString: String)
}