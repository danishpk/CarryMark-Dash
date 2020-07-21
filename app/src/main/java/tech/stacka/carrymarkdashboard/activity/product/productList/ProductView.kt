package tech.stacka.carrymarkdashboard.activity.product.productList

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.ProductListResponse

interface ProductView {
    abstract fun onProductListSuccess(apiResponse: ProductListResponse)
    abstract fun onProductListFailed(apiResponse: ResponseBody)
    abstract fun onProductListFailedServerError(string: String)
    abstract fun onProductListNull(apiResponse: ProductListResponse)
    abstract fun onDeleteProductSuccess(apiResponse: DefaultResponse)
    abstract fun onDeleteProductNull(apiResponse: DefaultResponse)
    abstract fun onDeleteProductFailed(apiResponse: ResponseBody)
    abstract fun onDeleteProductFailedServerError(toString: String)
    abstract fun onProductListMoreSuccess(apiResponse: ProductListResponse)
    abstract fun onProductListMoreNull(apiResponse: ProductListResponse)
    abstract fun onProductListMoreFailed(apiResponse: ResponseBody)
    abstract fun onProductListFailedMoreServerError(string: String)
}