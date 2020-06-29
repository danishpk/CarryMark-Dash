package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ArrProductList

data class ProductListResponse(
    val arrBrand: List<Any>,
    val arrCategory: List<Any>,
    val arrList: List<ArrProductList>,
    val blnAPIStatus: Boolean,
    val intAmountLimit: Int=0,
    val intLimit: Int=0,
    val intTotalCount: Int=0,
    val strSort: String="",
    val strSortActive: String=""
)