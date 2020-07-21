package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ArrLocationDetail

data class EmployeeLocationResponse(
    val arrList: List<ArrLocationDetail>,
    val blnAPIStatus: Boolean,
    val strCreatedTime: String,
    val strExecutiveId: String
)