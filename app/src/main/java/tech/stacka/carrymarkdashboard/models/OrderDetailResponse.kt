package tech.stacka.carrymarkdashboard.models

data class OrderDetailResponse(
    val _id: String,
    val arrDiscount: List<Any>,
    val arrProducts: List<ArrOrderProduct>,
    val arrTracks: List<ArrOrderTrack>,
    val blnAPIStatus: Boolean,
    val chrStatus: String,
    val dblTotalOrderAmount: Int,
    val objAddress: ObjOrderAddress,
    val strCreatedTime: String,
    val strCreatedUser: String,
    val strModePayment: String,
    val strOrderId: String,
    val strOrderStatus: String,
    val strOrderedUserId: String
)

data class ArrOrderProduct(
    val dblAmount: Int,
    val dblQty: Int,
    val strColor: String,
    val strImageUrl: String,
    val strName: String,
    val strProductId: String,
    val strSize: String
)

data class ArrOrderTrack(
    val _id: String,
    val datEstimateDelivery: Long,
    val intEstiDeliveryDays: Int,
    val strCreatedTime: String,
    val strCreatedUser: String,
    val strCurrentStatus: String,
    val strOrderDocId: String,
    val strOrderId: String
)

data class ObjOrderAddress(
    val strCity: String,
    val strDistrict: String,
    val strHouseArea: String,
    val strHouseNo: String,
    val strMobileNo: String,
    val strName: String,
    val strPinCode: String,
    val strState: String
)


data class ObjOrderExtras(
    val strColor: String,
    val strSize: String
)