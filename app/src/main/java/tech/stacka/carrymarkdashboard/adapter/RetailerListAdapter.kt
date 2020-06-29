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
    val executiveList: ArrayList<String>,
    val executiveIdList: ArrayList<String>
) :
    RecyclerView.Adapter<RetailerListAdapter.RetailersViewHolder>() {

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
        val adapter = ArrayAdapter(
            ctx,
            android.R.layout.simple_spinner_dropdown_item, executiveList
        )
        val retailer = retailerList[position]
        holder.displayName.text = retailer.strName
        holder.retId.text = retailer._id
        holder.mobile.text = retailer.strMobileNo
      //  holder.district.text = "District : " + retailer.arrAddress
        holder.whatsapp.text = retailer.strWhatsAppNumber
        holder.officeNo.text = retailer.strOfficeNumber
        //holder.address1.text = retailer.arrAddress
        //holder.address2.text = retailer.address2
       // holder.state.text = "State     : " + retailer.state
     //   holder.post.text = "PIN Code  : " + retailer.post
        holder.checkBox.isChecked = retailer.strActiveStatus == "A"
        holder.sp_executive.adapter = adapter
//        if (retailer.executiveId != "") {
//            holder.sp_executive.setSelection(executiveIdList.indexOf(retailer.executiveId))
//            val exeIndex = executiveIdList.indexOf(retailer.executiveId)
//            if (exeIndex >= 0)
//                holder.executiveName.text =
//                    "Executive : " + executiveList[exeIndex]
//        }
        holder.sp_executive.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (executiveIdList[pos] != "Executives *")
                        holder.executiveName.text = "Executive : " + executiveList[pos]
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
//        var mFunctions: FirebaseFunctions = FirebaseFunctions.getInstance()
//        holder.btUpdate.setOnClickListener { v ->
//            holder.btUpdate.isEnabled = false
//            val data = HashMap<String, Any>()
//            data["uid"] = retailer.uid
//            if (holder.checkBox.isChecked) {
//                data["status"] = "A"
//            } else {
//                data["status"] = "I"
//            }
//            data["executiveId"] = executiveIdList[holder.sp_executive.selectedItemPosition]
//            mFunctions.getHttpsCallable("verifyRetailer")
//                .call(data).addOnSuccessListener { httpsCallableResult ->
//                    val result = httpsCallableResult.data.toString()
//                    try {
//                        val g = Gson()
//                        val json =
//                            g.fromJson(result, Response::class.java)
//                        if (json.success) {
//                            Snackbar.make(
//                                v,
//                                "Updated Retailer",
//                                Snackbar.LENGTH_LONG
//                            ).show()
//                        } else {
//                            Snackbar.make(
//                                v,
//                                "Updation failed",
//                                Snackbar.LENGTH_LONG
//                            ).show()
//                        }
//                    } catch (e: Exception) {
//                        Snackbar.make(v, "Something went wrong", Snackbar.LENGTH_LONG)
//                            .show()
//                    }
//                    holder.btUpdate.isEnabled = true
//                }
//
//        }
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
//                        holder.exeId.visibility = View.GONE
//                        holder.distId.visibility = View.GONE
//                        holder.text3.visibility = View.GONE
//                        holder.text4.visibility = View.GONE
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
//                        holder.exeId.visibility = View.VISIBLE
//                        holder.distId.visibility = View.VISIBLE
//                        holder.text3.visibility = View.VISIBLE
//                        holder.text4.visibility = View.VISIBLE
                holder.text6.visibility = View.VISIBLE
                holder.text7.visibility = View.VISIBLE
                holder.text8.visibility = View.VISIBLE
                holder.text9.visibility = View.VISIBLE
            }
        }

    }

//    fun getLastItemId(): String {
//        return retailerList[retailerList.lastIndex].uid
//    }

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

        //        val exeId = view.tvExecutive
//        val distId = view.tvDistributor
//        val text3 = view.textView3
        val executiveName = view.tvExecutive
        val text5 = view.textView5
        val text6 = view.textView6
        val text7 = view.textView7
        val text8 = view.textView8
        val text9 = view.textView9
        val checkBox = view.chStatus
        val sp_executive = view.sp_executives

    }

}