package tech.stacka.carrymarkdashboard.activity.master.addMaster

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.UploadProductImageResponse

interface AddMasterView {
    abstract fun uploadImageSuccess(apiResponse: UploadProductImageResponse)
    abstract fun uploadImageNull(apiResponse: UploadProductImageResponse)
    abstract fun uploadImageFailed(apiResponse: ResponseBody)
    abstract fun uploadImageFailedServerError(toString: String)
    abstract fun addMasterSuccess(apiResponse: DefaultResponse)
    abstract fun addMasterNull(apiResponse: DefaultResponse)
    abstract fun addMasterFailed(apiResponse: ResponseBody)
    abstract fun addMasterFailedServerError(toString: String)
}