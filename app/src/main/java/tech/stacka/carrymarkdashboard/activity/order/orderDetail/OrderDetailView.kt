package tech.stacka.carrymarkexecutive.activity.order.orderDetail

import org.json.JSONArray
import tech.stacka.carrymarkdashboard.models.DefaultResponse
import tech.stacka.carrymarkdashboard.models.ErrCommon
import tech.stacka.carrymarkdashboard.models.OrderDetailResponse

interface OrderDetailView {
    abstract fun onOrderDetailSuccess(apiResponse: OrderDetailResponse)
    abstract fun onOrderDetailNull(apiResponse: OrderDetailResponse)
    abstract fun onOrderDetailFailed(apiResponse: ErrCommon)
    abstract fun onOrderServerFailed(toString: String)
    abstract fun onOrderUpdateSuccess(apiResponse: DefaultResponse)
    abstract fun onOrderUpdateNull(apiResponse: DefaultResponse)
    abstract fun onOrderUpdateFailed(apiResponse: JSONArray)
    abstract fun onOrderUpdateFailedServerError(toString: String)
}