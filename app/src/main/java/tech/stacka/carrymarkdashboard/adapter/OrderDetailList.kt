package tech.stacka.carrymarkdashboard.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.product_list_item.view.*
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.ArrOrderProduct
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailList (
    val context: Context,
    private val orderName: ArrayList<ArrOrderProduct>


    // private val clickListener: (ArrColorStock) -> Unit
) :
    RecyclerView.Adapter<OrderDetailList.OrderListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        return OrderListViewHolder(LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false))

    }

    override fun getItemCount(): Int {
        return orderName.size
    }

    override fun onBindViewHolder(
        holder: OrderDetailList.OrderListViewHolder,
        position: Int
    ) {
        holder.btdelete.visibility=View.GONE
        holder.btedit.visibility=View.GONE
        val orderProduct = orderName[position]
        Glide.with(context).load(orderProduct.strImageUrl).centerCrop().placeholder(R.drawable.ic_placeholder)
           .into(holder.image)
        holder.productName.text = orderProduct.strName
        holder.productId.visibility=View.VISIBLE
        holder.productId.text="ID : ${orderProduct.strOGProductId}"
        holder.cat.text = " Quantity  : ${orderProduct.dblQty}"
        holder.pId.text = "Total Amount   : ${orderProduct.dblAmount*orderProduct.dblQty}₹"
        holder.price.text = "${orderProduct.dblAmount}₹"
        holder.offrQty.text="Offer Quantity : ${orderProduct.dblOfferQty}"
        //  val category = ColorList[position]
//        holder.colorView.setBackgroundColor(Color.parseColor("#"+colorCode[position]))
//        holder.colorName.text = colorName[position]



    }

    class OrderListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.ivProduct
        val productName = view.tvProductName
        val cat = view.tvProductCat
        val pId = view.tvProductId
        val price = view.tvProductPrice
        val btdelete=view.btDelete;
        val btedit=view.btEdit
        val offrQty=view.tvProductOffrQty
        val productId=view.tvProductOgId
        //val btCart = view.btCart

    }


}