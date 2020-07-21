package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ClnBrand
import tech.stacka.carrymarkdashboard.models.data.ClnCategories
import tech.stacka.carrymarkdashboard.models.data.ClnCategory
import tech.stacka.carrymarkdashboard.models.data.ClnMaterial

data class FilterCategoryResponse(
    val cln_brand: List<ClnBrand>,
    val cln_category: List<ClnCategory>,
    val cln_material: List<ClnMaterial>
)