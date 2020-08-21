package tech.stacka.carrymarkdashboard.models
import tech.stacka.carrymarkdashboard.models.data.ObjAddress
data class OrderDetailResponse(
    val _id: String,
    val arrDiscount: List<Any>,
    val arrProducts: List<ArrOrderProduct>,
    val arrTracks: List<ArrOrderTrack>,
    val blnAPIStatus: Boolean,
    val chrStatus: String,
    val dblOfferAmount: Double,
    val dblTotalAmount: Double,
    val dblTotalOrderAmount: Double,
    val dblTotalPrice:Double,
    val dblDiscountPrice:Double,
    val objAddress: ObjAddress,
    val strCreatedTime: String,
    val strCreatedUser: String,
    val strDistributerId: Any,
    val strExecutiveId: Any,
    val strModePayment: String,
    val strOrderId: String,
    val strOrderStatus: String,
    val strOrderedUserId: String,
    val strUserType: String,
    val strDistributerName: String,
    val strGSTNo: String,
    val strPinCode: String,
    val strShopName: String,
    val strUserId: String,
    val strNote: String,
    val strMobileNo:String,
    val strExecutiveName:String
)

data class ArrOrderProduct(
    val dblAmount: Int,
    val dblQty: Int,
    val dblSellingPrice: Int,
    val strCreatedTime: String,
    val strImageUrl: String,
    val strName: String,
    val strProductId: String,
    val strOGProductId: String,
    val dblOfferQty :Int
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