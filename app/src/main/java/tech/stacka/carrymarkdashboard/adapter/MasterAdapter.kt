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
import kotlinx.android.synthetic.main.master_list_item.view.*
import kotlinx.android.synthetic.main.product_list_item.view.*
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.product.productDetails.ProductDetailActivity
import tech.stacka.carrymarkdashboard.models.data.ArrProductList
import tech.stacka.carrymarkdashboard.models.data.ClnBrand
import tech.stacka.carrymarkdashboard.models.data.ClnCategories
import java.util.*

class MasterAdapter(val ctx: Context, val categoryList: ArrayList<ClnCategories>)
    : RecyclerView.Adapter<MasterAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder
    {
        return ProductViewHolder(LayoutInflater.from(ctx).inflate(R.layout.master_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = categoryList[position]

        Glide.with(ctx).load(product.strImgUrl_0).centerCrop().placeholder(R.drawable.ic_placeholder)
            .into(holder.image)
        holder.productName.text = product.strName.toUpperCase(Locale.ENGLISH)

    }


    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.ivMasterList
        val productName = view.tvMasterListName

    }


}