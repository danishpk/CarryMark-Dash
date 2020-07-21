package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ArrOrderList

data class OrderListResponse(
    val arrList: List<ArrOrderList>,
    val blnAPIStatus: Boolean,
    val intTotalCount: Int
)