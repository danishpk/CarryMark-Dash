package tech.stacka.carrymarkdashboard.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import tech.stacka.carrymarkdashboard.R
import java.util.*
import kotlin.collections.ArrayList


class FilterOrderExecutiveAdapter(var mContext: Context, data: ArrayList<String>, dataIds:ArrayList<String>,
                                  dtInterface:DataTransferInterface,
                                  var selctedExeIds: ArrayList<String>
) : ArrayAdapter<String>(mContext, R.layout.filter_category_list) {
    private val data: ArrayList<String>
    private val dataIds:ArrayList<String>
    var dtInterface: DataTransferInterface
    private val mItemChecked: BooleanArray
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.filter_category_list, parent, false)
            holder = ViewHolder()
            holder.mName = convertView!!.findViewById<View>(R.id.subjectName_textView) as TextView?
            holder.checkBox = convertView.findViewById<View>(R.id.subjectCheckbox) as CheckBox?
            //dtInterface.setValues(filteredCategory)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        for(i in selctedExeIds.indices){
            if(dataIds[position]==selctedExeIds[i])
                mItemChecked[position] = true
            Log.e("dataPosition",data[position])
            Log.e("filteredPosition",selctedExeIds[i])
        }


        holder.mName!!.text = this.data[position].toString()
        holder.checkBox!!.setOnCheckedChangeListener(null)
        holder.checkBox!!.isChecked = mItemChecked[position]
        holder.checkBox!!.tag = position
        holder.checkBox!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked.also { mItemChecked[position] = it }) {
                selctedExeIds.add(dataIds[position].toString())
                dtInterface.setExeValue(selctedExeIds)

            } else {
                selctedExeIds.remove(dataIds[position].toString())
                dtInterface.setExeValue(selctedExeIds)
            }
        }
        return convertView
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun itemIsChecked(position: Int): Boolean {
        return mItemChecked[position]
    }

    internal class ViewHolder {
        var checkBox: CheckBox? = null
        var mName: TextView? = null
    }

    init {
        this.dataIds=dataIds
        this.dtInterface=dtInterface
        this.data = data
        this.selctedExeIds=selctedExeIds
        mItemChecked = BooleanArray(data.size)
//        for (i in filteredCategory.indices) {
//         //   if(filteredCategory[i]==data[i])
//            mItemChecked[i] = true
//          filteredCategory.add(data[i].toString())
//        }
    }

    interface DataTransferInterface {
        fun setExeValue(arrSelectedExecutiveIds: ArrayList<String>)
    }
}