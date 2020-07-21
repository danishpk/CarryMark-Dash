package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ORDER
import tech.stacka.carrymarkdashboard.models.data.ObjUserReport
import tech.stacka.carrymarkdashboard.models.data.SALE

data class ReportResponse(
    val ORDER: ORDER,
    val SALE: SALE,
    val blnAPIStatus: Boolean,
    val objUser: ObjUserReport
)