package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ArrDistributerList

data class DistributerListResponse(
    val arrList: List<ArrDistributerList>,
    val intTotalCount: Int=0,
    val strType: String=""
)