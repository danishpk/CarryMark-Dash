package tech.stacka.carrymarkdashboard.models

data class LoginResponse(
    val _id: String?,
    val chrStatus: String?,
    val strAddress: String?,
    val strMobileNo: String?,
    val strName: String?,
    val strPinCode: String?,
    val strToken: String?,
    val blnAPIStatus:Boolean
)