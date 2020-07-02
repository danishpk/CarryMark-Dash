package tech.stacka.carrymarkdashboard.models

import tech.stacka.carrymarkdashboard.models.data.ArrColorStock
import tech.stacka.carrymarkdashboard.models.data.ArrSizeStock

data class ProductDetailResponse(
    val _id: String="",
    val arrColorStock: List<ArrColorStock>,
    val arrImageUrl: List<String>,
    val arrScheme: List<Any>,
    val arrSizeStock: List<ArrSizeStock>,
    val blnAPIStatus: Boolean,
    val chrStatus: String="",
    val dblMRP: Int=0,
    val dblRetailerPrice: Int=0,
    val dblSellingPrice: Int=0,
    val dblTotalSales: Int=0,
    val dblTotalStock: Int=0,
    val intEstiDeliveryDays: Int=0,
    val strBrandId: String,
    val strCategoryId: String,
    val strCreatedTime: String,
    val strCreatedUser: String,
    val strDescription: String,
    val strGenderCategory: String,
    val strImageUrl: String,
    val strMaterial: String,
    val strName: String,
    val strProductId: String,
    val strSubCategory: String,
    val strTargetType: String,
    val strUnit: String
)