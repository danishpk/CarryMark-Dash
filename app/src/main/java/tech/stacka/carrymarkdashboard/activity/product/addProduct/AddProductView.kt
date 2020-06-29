package tech.stacka.carrymarkdashboard.activity.product.addProduct

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.AddProductCategoryResponse
import tech.stacka.carrymarkdashboard.models.AddProductResponse
import tech.stacka.carrymarkdashboard.models.AddSizeColorResponse
import tech.stacka.carrymarkdashboard.models.UploadProductImageResponse

interface AddProductView {
    abstract fun addProductSuccess(apiResponse: AddProductResponse)
    abstract fun addProductFailed(apiResponse: ResponseBody)
    abstract fun addProductFailedServerError(string: String)
    abstract fun uploadImageSuccess(apiResponse: UploadProductImageResponse)
    abstract fun uploadImageFailed(apiResponse: ResponseBody)
    abstract fun uploadImageFailedServerError(string: String)
    abstract fun onProductCategoryListSuccess(apiResponse: AddProductCategoryResponse)
    abstract fun onProductCategoryListFailed(apiResponse: ResponseBody)
    abstract fun onProductCategoryListFailedServerError(string: String)
    abstract fun addProductNull(apiResponse: AddProductResponse)
    abstract fun uploadImageNull(apiResponse: UploadProductImageResponse)
    abstract fun onProductCategoryListNull(apiResponse: AddProductCategoryResponse)
    abstract fun onAddSizeColorSuccess(apiResponse: AddSizeColorResponse)
    abstract fun onAddSizeColorNull(apiResponse: AddSizeColorResponse)
    abstract fun onAddSizeColorFailed(apiResponse: ResponseBody)
    abstract fun onAddSizeColorFailedServerError(string: String)
}