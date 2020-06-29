package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ClnBrand
import tech.stacka.carrymarkdashboard.models.data.ClnCategory

data class FilterCategoryResponse(
    val cln_brand: List<ClnBrand>,
    val cln_category: List<ClnCategory>
)