package tech.stacka.carrymarkdashboard.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.master_list_item.view.*
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.data.ClnCategories
import java.util.*

class MasterAdapter(val ctx: Context, val categoryList: ArrayList<ClnCategories>,var dataInterface: DataTransferInterfaceMaster)
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
        if(product.strImgUrl_0!=null ) {
            if (!product.strImgUrl_0.equals("")) {
                Glide.with(ctx).load(product.strImgUrl_0).centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.image)

            }
        }
        if(!product.strColorCode.equals("")){
            holder.image.setBackgroundColor(Color.parseColor("#"+product.strColorCode));
        }
        if(!product.strParentCategory.equals("")){
            holder.parentName.visibility=View.VISIBLE
            holder.parentName.text=product.strParentCategory

        }else{
            holder.parentName.visibility=View.GONE
        }

        holder.productName.text = product.strName.toUpperCase(Locale.ENGLISH)

    holder.delMaster.setOnClickListener {

        Snackbar.make(it, "Are You Sure Want To Delete", Snackbar.LENGTH_LONG)
            .setAction("Yes", View.OnClickListener {
                dataInterface.setValues(product._id)
            }).show()
    }
    }



    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.ivMasterList
        val productName = view.tvMasterListName
        val parentName = view.tvParentListName
        val delMaster = view.btDeleteCategory

    }

    interface DataTransferInterfaceMaster {
        fun setValues(strMasterId:String)
    }


}