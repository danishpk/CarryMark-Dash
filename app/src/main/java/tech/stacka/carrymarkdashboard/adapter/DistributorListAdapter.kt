package tech.stacka.carrymarkdashboard.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.distributor_list_item.view.*
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.distributer.distributerDetail.DistributerDetailActivity
import tech.stacka.carrymarkdashboard.activity.employee.employeeDetail.EmployeeDetailActivity
import tech.stacka.carrymarkdashboard.models.data.ArrDistributerList
import java.util.*

class DistributorListAdapter(
    val ctx: Context,
    val distributorList: ArrayList<ArrDistributerList>,
    val selectUser: Boolean,
    val activity: Activity
) :
    RecyclerView.Adapter<DistributorListAdapter.DistributorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistributorViewHolder {
        return DistributorViewHolder(
            LayoutInflater.from(ctx).inflate(
                R.layout.distributor_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return distributorList.size
    }

    override fun onBindViewHolder(holder: DistributorViewHolder, position: Int) {
        val distributor = distributorList[position]
        holder.displayName.text = distributor.strName
        holder.distId.text = distributor._id
        holder.district.text=distributor.strEmail
       // holder.district.text = distributor.district
        holder.mobile.text = distributor.strMobileNo
            holder.itemView.setOnClickListener {
                val i = Intent(ctx, DistributerDetailActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                i.putExtra("distributorId", distributorList[position]._id)
                ctx.startActivity(i)

        }
    }

    fun getLastItemId(): String {
        return distributorList[distributorList.lastIndex]._id
    }

    fun addAll(newOrders: ArrayList<ArrDistributerList>) {
        val initialSize = distributorList.size
        distributorList.addAll(newOrders)
        notifyItemRangeInserted(initialSize, newOrders.size)
    }

    class DistributorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val displayName = view.tvDisplayName
        val distId = view.tvRetId
        val district = view.tvDistrict
        val mobile = view.tvMobile
    }

}