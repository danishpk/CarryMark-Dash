package tech.stacka.carrymarkdashboard.activity.product.filterProduct

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.FilterCategoryResponse

interface ProductFilterView {
    abstract fun onFilterCategoryListSuccess(apiResponse: FilterCategoryResponse)
    abstract fun onFilterProductCategoryListFailed(apiResponse: ResponseBody)
    abstract fun onFilterProductCategoryServerFailed(string: String)
    abstract fun onFilterProductCategoryListNull(apiResponse: FilterCategoryResponse)
}