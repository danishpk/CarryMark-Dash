package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ArrRetailerList

data class RetailerListResponse(
    val arrList: List<ArrRetailerList>,
    val intTotalCount: Int=0,
    val strType: String=""
)