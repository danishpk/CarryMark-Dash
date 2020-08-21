package tech.stacka.carrymarkdashboard.models.data

data class ArrLocationDetail(
    val _id: String,
    val coordinates: List<Double>,
    val strCreatedTime: String,
    val strTime: String,
    val strExecutiveId: String,
    val type: String
)