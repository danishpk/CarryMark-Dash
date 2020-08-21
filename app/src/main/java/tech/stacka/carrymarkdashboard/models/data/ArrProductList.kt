package tech.stacka.carrymarkdashboard.models.data

data class ArrProductList(
    val _id: String,
    val dblMRP: Int,
    val dblRetailerPrice: Int,
    val dblSellingPrice: Int,
    val objImageUrls: ObjImageUrls,
    val strBrandId: String,
    val strCategoryId: String,
    val strDescription: String,
    val strGenderCategory: String,
    val strImageUrl: String,
    val strName: String,
    val strProductId: String,
    val dblTotalSales: Int=0,
    val dblTotalStock:Int
)