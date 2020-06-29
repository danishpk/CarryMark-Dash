package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList

data class EmployeeListResponse(
    val arrList: List<ArrEmployeeList>,
    val intTotalCount: Int=0,
    val strType: String=""
)