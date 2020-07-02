package tech.stacka.carrymarkdashboard.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.product_list_item.view.*
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.product.productDetails.ProductDetailActivity
import tech.stacka.carrymarkdashboard.models.data.ArrProductList
import java.util.*

class ProductListAdapter(val ctx: Context, val productList: ArrayList<ArrProductList>, val lastItemCount:String,
                         var dataInterface:DataTransferInterfacePr) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder
    {
        return ProductViewHolder(LayoutInflater.from(ctx).inflate(R.layout.product_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        Glide.with(ctx).load(product.strImageUrl).centerCrop().placeholder(R.drawable.ic_placeholder)
            .into(holder.image)
        holder.productName.text = product.strName.toUpperCase(Locale.ENGLISH)
        holder.cat.text = product.strGenderCategory
        holder.pId.text = product.strProductId
        holder.price.text = "₹ ${product.dblSellingPrice}"
        holder.btEdit.setOnClickListener {
//            val i = Intent(ctx, EditProductActivity::class.java)
//            i.putExtra("skuId", product.skuId)
//            ctx.startActivity(i)
        }
        holder.btDelete.setOnClickListener { view ->
            Snackbar.make(view, "Are You Sure Want To Delete", Snackbar.LENGTH_LONG)
                .setAction("Yes", View.OnClickListener {
                dataInterface.setValues(product._id)
                }).show()

        }
        holder.itemView.setOnClickListener {
            val intent = Intent(ctx, ProductDetailActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("productId",product._id)
            ctx.startActivity(intent)
        }
    }

    fun getLastItemId(): String {
        return lastItemCount
    }

//    fun addAll(newOrders: ArrayList<ProductListData>) {
//        val initialSize = productList.size
//        productList.addAll(newOrders)
//        notifyItemRangeInserted(initialSize, newOrders.size)
//    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.ivProduct
        val productName = view.tvProductName
        val cat = view.tvProductCat
        val pId = view.tvProductId
        val price = view.tvProductPrice
        val btEdit = view.btEdit
        val btDelete = view.btDelete
    }

    interface DataTransferInterfacePr {
        fun setValues(strProductId:String)
    }

}