package tech.stacka.carrymarkdashboard.activity.order.orderList

import okhttp3.ResponseBody
import tech.stacka.carrymarkdashboard.models.OrderListResponse

interface OrderListView {
    abstract fun onOrderListSuccess(apiResponse: OrderListResponse)
    abstract fun onOrderListNull(apiResponse: OrderListResponse)
    abstract fun onOrderListFailed(apiResponse: ResponseBody)
    abstract fun onOrderServerFailed(toString: String)


}