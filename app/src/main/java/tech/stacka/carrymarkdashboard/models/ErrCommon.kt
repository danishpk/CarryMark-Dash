package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ObjErrorDetails

data class ErrCommon(
    val objDetails: ObjErrorDetails,
    val strMessage: String=""
)