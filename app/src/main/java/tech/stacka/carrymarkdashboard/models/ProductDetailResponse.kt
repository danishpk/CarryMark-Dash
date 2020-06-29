package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ArrProductDetailColorStock
import tech.stacka.carrymarkdashboard.models.data.ArrProductDetailSizeStock
import tech.stacka.carrymarkdashboard.models.data.ObjImageUrls

data class ProductDetailResponse(
    val _id: String="",
    val arrColorStock: List<ArrProductDetailColorStock>,
    val arrScheme: List<Any>,
    val arrSizeStock: List<ArrProductDetailSizeStock>,
    val chrStatus: String="",
    val dblMRP: Int=0,
    val dblRetailerPrice: Int=0,
    val dblSellingPrice: Int=0,
    val dblTotalSales: Int=0,
    val dblTotalStock: Int=0,
    val intEstiDeliveryDays: Int=0,
    val objImageUrls: ObjImageUrls,
    val strBrandId: String="",
    val strCategoryId: String="",
    val strCreatedTime: String="",
    val strCreatedUser: String="",
    val strDescription: String="",
    val strGenderCategory: String="",
    val strImageUrl: String="",
    val strName: String="",
    val strProductId: String="",
    val strTargetType: String="",
    val strUnit: String=""
)