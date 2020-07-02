package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.*

data class GetMasterResponse(
    val blnAPIStatus: Boolean,
    val cln_brand: List<ClnCategories>,
    val cln_category: List<ClnCategories>,
    val cln_color: List<ClnCategories>,
    val cln_location: List<ClnCategories>,
    val cln_material: List<ClnCategories>,
    val cln_size: List<ClnCategories>,
    val cln_sub_category: List<ClnCategories>
)