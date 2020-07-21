package tech.stacka.carrymarkdashboard.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.product_list_item.view.*
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.product.productDetail.ProductDetailActivity
import tech.stacka.carrymarkdashboard.activity.product.productUpdate.ProductUpdateActivity
import tech.stacka.carrymarkdashboard.models.data.ArrProductList
import java.util.*

class ProductListAdapter(val ctx: Context, val productList: ArrayList<ArrProductList>, val lastItemCount:Int,
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
        holder.cat.text = "${product.dblTotalSales} sales"
        holder.pId.text = product.strProductId
        holder.price.text = "₹ ${product.dblSellingPrice}"
        holder.btEdit.setOnClickListener {
            val intent = Intent(ctx, ProductUpdateActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("productId",product._id)
            ctx.startActivity(intent)
        }
        holder.btDelete.setOnClickListener { view ->
            Snackbar.make(view, "Are You Sure Want To Delete", Snackbar.LENGTH_LONG)
                .setAction("Yes", View.OnClickListener {
                dataInterface.setValues(product._id)
                }).show()

        }
        holder.itemView.setOnClickListener {
            val intent = Intent(ctx, ProductDetailActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            intent.putExtra("productId",product._id)
            ctx.startActivity(intent)
        }
    }

    fun getLastItemId(): Int {
        return lastItemCount
    }

    fun add(r: ArrProductList) {
        productList.add(r)
        notifyItemInserted(productList.size - 1)
    }

    fun addAll(data: ArrayList<ArrProductList>) {
        for (result in data) {
            add(result)
        }
    }

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