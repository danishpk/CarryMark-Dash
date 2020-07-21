package tech.stacka.carrymarkdashboard.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.order_list_item.view.*
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.order.orderDetail.OrderDetailActivity
import tech.stacka.carrymarkdashboard.models.data.ArrOrderList

class OrderListAdapter(val ctx: Context, val orderList: ArrayList<ArrOrderList>) :
    RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            LayoutInflater.from(ctx).inflate(
                R.layout.order_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.orderId.text = order.strOrderId
        holder.orderItems.text = "${order.arrProducts.size} Products"
        holder.orderTime.text =order.strCreatedTime
          //  SimpleDateFormat("dd/MM/yyyy \nHH:mm a").format(order.!!.toDate())
        holder.retailerId.text = order.strOrderedUserId
        holder.orderStatus.text = order.strOrderStatus
        holder.shopName.text = ""
        holder.itemView.setOnClickListener {
            val intent = Intent(ctx, OrderDetailActivity::class.java)
            intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("orderID", order.strOrderId)
            ctx.startActivity(intent)

        }
    }

//    fun getLastItemId(): Timestamp {
//        return orderList[orderList.lastIndex].orderTime!!
//    }

//    fun addAll(newPosts: ArrayList<Orders>) {
//        val initialSize = orderList.size
//        orderList.addAll(newPosts)
//        notifyItemRangeInserted(initialSize, newPosts.size)
//    }


    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId = view.tv_orderId
        val orderItems = view.tv_OrderItems
        val orderTime = view.tv_OrderTime
        val retailerId = view.tv_RetailerId
        val orderStatus = view.tv_OrderStatus
        val shopName = view.tv_ShopName
    }

}