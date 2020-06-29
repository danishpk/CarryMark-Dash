package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ArrAddProductCategory

data class AddProductCategoryResponse(
    val arrList: List<ArrAddProductCategory>,
    val blnAPIStatus: Boolean
)