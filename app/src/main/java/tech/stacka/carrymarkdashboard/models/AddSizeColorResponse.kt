package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ClnColor
import tech.stacka.carrymarkdashboard.models.data.ClnSize

data class AddSizeColorResponse(
    val blnAPIStatus: Boolean,
    val cln_color: List<ClnColor>,
    val cln_size: List<ClnSize>
)