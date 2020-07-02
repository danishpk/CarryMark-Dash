package tech.stacka.carrymarkdashboard.activity.product.productDetails

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.*

interface ProductDetailView {
    abstract fun onProductDetailSuccess(apiResponse: ProductDetailResponse)
    abstract fun onProductDetailNull(apiResponse: ProductDetailResponse)
    abstract fun onProductDetailFailed(apiResponse: ResponseBody)
    abstract fun onProductDetailServerFailed(toString: String)
    abstract fun updateProductSuccess(apiResponse: AddProductResponse)
    abstract fun updateProductNull(apiResponse: AddProductResponse)
    abstract fun updateProductFailed(apiResponse: ResponseBody)
    abstract fun updateProductFailedServerError(toString: String)
    abstract fun uploadImageSuccess(apiResponse: UploadProductImageResponse)
    abstract fun uploadImageNull(apiResponse: UploadProductImageResponse)
    abstract fun uploadImageFailed(apiResponse: ResponseBody)
    abstract fun uploadImageFailedServerError(toString: String)
    abstract fun onProductCategoryListSuccess(apiResponse: AddProductCategoryResponse)
    abstract fun onProductCategoryListNull(apiResponse: AddProductCategoryResponse)
    abstract fun onProductCategoryListFailed(apiResponse: ResponseBody)
    abstract fun onProductCategoryListFailedServerError(string: String)
    abstract fun onAddSizeColorSuccess(apiResponse: AddSizeColorResponse)
    abstract fun onAddSizeColorNull(apiResponse: AddSizeColorResponse)
    abstract fun onAddSizeColorFailed(apiResponse: ResponseBody)
    abstract fun onAddSizeColorFailedServerError(string: String)
}