package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeDetail

data class EmployeeDetailResponse(
    val arrList: List<ArrEmployeeDetail>,
    val blnAPIStatus: Boolean,
    val intTotalCount: Int,
    val strDocId: String,
    val strType: String
)