package tech.stacka.carrymarkdashboard.models.data

data class ORDER(
    val CANCEL: Int,
    val CONFIRM: Int,
    val DELIVERED: Int,
    val PENDING: Int,
    val REFUNDED: Int,
    val RETURNED: Int,
    val SHIPPED: Int,
    val TOTAL: Int
)