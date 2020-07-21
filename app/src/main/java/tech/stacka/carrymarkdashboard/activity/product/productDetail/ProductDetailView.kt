package tech.stacka.carrymarkdashboard.activity.product.productDetail

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.ProductDetailResponse

interface ProductDetailView {
    abstract fun onProductDetailSuccess(apiResponse: ProductDetailResponse)
    abstract fun onProductDetailNull(apiResponse: ProductDetailResponse)
    abstract fun onProductDetailFailed(apiResponse: ResponseBody)
    abstract fun onProductDetailServerFailed(toString: String)
}