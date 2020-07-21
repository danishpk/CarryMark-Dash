package tech.stacka.carrymarkdashboard.models.data

data class ArrOrderList(
    val _id: String,
    val arrDiscount: List<Any>,
    val arrProducts: List<ArrProduct>,
    val chrStatus: String,
    val dblTotalOrderAmount: Any,
    val objAddress: ObjAddress,
    val strCreatedTime: String,
    val strCreatedUser: String,
    val strModePayment: String,
    val strOrderId: String,
    val strOrderStatus: String,
    val strOrderedUserId: String
)