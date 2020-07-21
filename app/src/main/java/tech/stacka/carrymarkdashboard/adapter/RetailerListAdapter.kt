package tech.stacka.carrymarkdashboard.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.retailer_list_item.view.*
import tech.stacka.carrymarkdashboard.R
import tech.stacka.carrymarkdashboard.models.data.ArrRetailerList

import java.util.*

class RetailerListAdapter(
    val ctx: Context,
    val retailerList: ArrayList<ArrRetailerList>,
    val selectUser: Boolean,
    val activity: Activity,
    val employeeList: ArrayList<String>,
    val employeeIdList: ArrayList<String>,
    val distributerList: ArrayList<String>,
    val distributerIdList: ArrayList<String>,
    var dtInterface: DataTransferInterfaceRetailer
) : RecyclerView.Adapter<RetailerListAdapter.RetailersViewHolder>() {
    var strActive="";
    var strDistributorName:String=""
    var strExecutiveName:String=""
    var strDistributorId:String=""
    var strExecutiveId:String=""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetailersViewHolder {
        return RetailersViewHolder(
            LayoutInflater.from(ctx).inflate(
                R.layout.retailer_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return retailerList.size
    }

    override fun onBindViewHolder(holder: RetailersViewHolder, position: Int) {
        val adapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, employeeList)
        val adapter2 = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, distributerList)
        val retailer = retailerList[position]
        holder.displayName.text = retailer.strName
        holder.retId.text = retailer._id
        holder.mobile.text = retailer.strMobileNo
        holder.whatsapp.text = retailer.strWhatsAppNumber
        holder.officeNo.text = retailer.strOfficeNumber
        holder.checkBox.isChecked = retailer.strActiveStatus == "A"
        holder.sp_executive.adapter = adapter
        holder.sp_distributer.adapter=adapter2

        if (retailerList.get(position).strExecutiveId!= null) {
            if(holder.checkBox.isChecked){
                strActive="A"

            }else{
                strActive="I"
            }

            holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    strActive="A"
                }else{
                    strActive="I"
                }
            }
            holder.sp_executive.setSelection(employeeIdList.indexOf(retailer.strExecutiveId))
            val exeIndex = employeeIdList.indexOf(retailer.strExecutiveId)

            if (exeIndex >= 0) {
                holder.executiveName.text = "Executive : " + employeeList[exeIndex]
                strExecutiveName = employeeList[exeIndex]
                strExecutiveId = employeeIdList[exeIndex]
            }
        }

        holder.sp_executive.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {

                    if (employeeIdList[pos] != "Executives *") {
                        holder.executiveName.text = "Executive : " + employeeList[pos]
                        strExecutiveId = employeeIdList[pos]
                        strExecutiveName = employeeList[pos]
                    }
                }

            }



        if (retailerList.get(position).strDistributerId != null) {
            holder.sp_distributer.setSelection(distributerIdList.indexOf(retailer.strDistributerId))
            val exeIndex = distributerIdList.indexOf(retailer.strDistributerId)
            if (exeIndex >= 0) {
                holder.distributerName.text = "Distributer : " + distributerList[exeIndex]
                strDistributorId = distributerIdList[exeIndex]
                strDistributorName = distributerList[exeIndex]
            }
        }
        holder.sp_distributer.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {

                    if (distributerIdList[pos] != "Distributers * *") {
                        holder.distributerName.text = "Distributer : " + distributerList[pos]
                        strDistributorName = distributerList[pos]
                        strDistributorId = distributerIdList[pos]
                    }
                }

            }



        if (selectUser) {
            holder.itemView.setOnClickListener {
                val intent = Intent()
                intent.putExtra("uId", retailer._id)
                intent.putExtra("token", retailer._id)
                activity.setResult(2, intent)
                activity.finish()
            }
        }

        holder.btUpdate.setOnClickListener {
            dtInterface.setRetailerValues(strExecutiveId,strExecutiveName,strDistributorId,
                strDistributorName,strActive,retailerList.get(position)._id)
        }

        holder.btmMore.setOnClickListener {
            if (holder.district.getVisibility() == View.VISIBLE) {
                holder.btmMore.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp)
                holder.district.visibility = View.GONE
                holder.whatsapp.visibility = View.GONE
                holder.officeNo.visibility = View.GONE
                holder.address1.visibility = View.GONE
                holder.address2.visibility = View.GONE
                holder.state.visibility = View.GONE
                holder.post.visibility = View.GONE
                holder.text6.visibility = View.GONE
                holder.text7.visibility = View.GONE
                holder.text8.visibility = View.GONE
                holder.text9.visibility = View.GONE
            } else {
                holder.btmMore.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp)
                holder.district.visibility = View.VISIBLE
                holder.whatsapp.visibility = View.VISIBLE
                holder.officeNo.visibility = View.VISIBLE
                holder.address1.visibility = View.VISIBLE
                holder.address2.visibility = View.VISIBLE
                holder.state.visibility = View.VISIBLE
                holder.post.visibility = View.VISIBLE
                holder.text6.visibility = View.VISIBLE
                holder.text7.visibility = View.VISIBLE
                holder.text8.visibility = View.VISIBLE
                holder.text9.visibility = View.VISIBLE
            }
        }

    }


    fun addAll(newOrders: ArrayList<ArrRetailerList>) {
        val initialSize = retailerList.size
        retailerList.addAll(newOrders)
        notifyItemRangeInserted(initialSize, newOrders.size)
    }

    class RetailersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btmMore = view.btmMore
        val btUpdate = view.btUpdate
        val address1 = view.tvAddress1
        val address2 = view.tvAddress2
        val displayName = view.tvDisplayName
        val retId = view.tvRetId
        val district = view.tvDistrict
        val mobile = view.tvMobile
        val whatsapp = view.tvWhatsapp
        val officeNo = view.tvOfficeNo
        val state = view.tvState
        val post = view.tvPost
        val distributerName=view.tv_distributer
        val executiveName = view.tvExecutive
        val text5 = view.textView5
        val text6 = view.textView6
        val text7 = view.textView7
        val text8 = view.textView8
        val text9 = view.textView9
        val checkBox = view.chStatus
        val sp_executive = view.sp_executives
        val sp_distributer = view.sp_distributers

    }
    interface DataTransferInterfaceRetailer {
        fun setRetailerValues(strExecutiveId:String,strExecutiveName:String,strDistributorId:String,
                              strDistributorName:String,strActive:String,strRetailerId:String)
    }

}