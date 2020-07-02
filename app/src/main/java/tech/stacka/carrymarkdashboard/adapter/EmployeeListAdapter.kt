package tech.stacka.carrymarkdashboard.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.employee_list_item.view.*
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.activity.employee.employeeDetail.EmployeeDetailActivity
import tech.stacka.carrymarkdashboard.models.data.ArrEmployeeList
import java.util.*

class EmployeeListAdapter(
    val ctx: Context,
    val employeeList: ArrayList<ArrEmployeeList>,
    val activity: Activity,
    val selectUser: Boolean
) :
    RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder(
            LayoutInflater.from(ctx).inflate(
                R.layout.employee_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }
    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
       // val employee = employeeList[position]
        if (employeeList.get(position).chrStatus.equals("N")) {
            Glide.with(ctx).load(R.drawable.ic_grey_round)
                .into(holder.absent)
        } else {
            Glide.with(ctx).load(R.drawable.ic_grey_round)
                .into(holder.present)
        }
       // val percentage = (employee.completedTarget*100)/employee.target
      //  holder.target.text = "$percentage %"
        holder.name.text = employeeList.get(position).strName
        holder.itemView.setOnClickListener {
            if (!selectUser) {
                val i = Intent(ctx, EmployeeDetailActivity::class.java)
                i.putExtra("employeeId", employeeList.get(position)._id)
                ctx.startActivity(i)
            } else {
                val intent = Intent()
                intent.putExtra("uId", employeeList.get(position)._id)
                intent.putExtra("token", employeeList.get(position).strCreatedUser)
                activity.setResult(2, intent)
                activity.finish()
            }
        }
    }

    fun getLastItemId(): String {
        return employeeList[employeeList.lastIndex]._id
    }

    fun addAll(newOrders: ArrayList<ArrEmployeeList>) {
        val initialSize = employeeList.size
        employeeList.addAll(newOrders)
        notifyItemRangeInserted(initialSize, newOrders.size)
    }

    class EmployeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val present = view.ivPresent
        val absent = view.ivAbsent
        val name = view.tv_name
        val target = view.tvTarget
    }

}