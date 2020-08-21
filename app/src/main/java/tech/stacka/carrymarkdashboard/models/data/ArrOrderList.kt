package tech.stacka.carrymarkdashboard.models.data

data class ArrOrderList(
    val _id: String="",
    val arrDiscount: List<String>,
    val arrProducts: List<ArrProduct>,
    val chrStatus: String="",
    val dblTotalOrderAmount: Double=0.0,
    val dblDiscount: Double,
    val dblDiscountPrice: Double,
    val dblOfferAmount: Double,
    val dblTotalAmount: Double,
    val dblTotalPrice: Double,
    val objAddress: ObjAddress,
    val strCreatedTime: String="",
    val strCSVDate:String="",
    val strCreatedUser: String="",
    val strModePayment: String="",
    val strOrderId: String="",
    val strOrderStatus: String="",
    val strOrderedUserId: String="",
    val strShopName:String="",
    val strName:String="",
    val strExecutiveName:String="",
    val strState:String
)