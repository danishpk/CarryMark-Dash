package tech.stacka.carrymarkdashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import tech.stacka.carrymarkdashboard.R
import java.util.*


class FilterCategoryAdapter(var mContext: Context, data: ArrayList<String>, dtInterface:DataTransferInterface) : ArrayAdapter<String>(mContext, R.layout.filter_category_list) {
    private val data: ArrayList<String>
    var dtInterface: DataTransferInterface
    var filterCategory: MutableList<String> = ArrayList()
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
            dtInterface.setValues(filterCategory)
            convertView.setTag(holder)
        } else {
            holder =
                convertView.tag as ViewHolder
        }
    holder.mName!!.setText(this.data[position].toString())
        holder.checkBox!!.setOnCheckedChangeListener(null)
        holder.checkBox!!.isChecked = mItemChecked[position]
        holder.checkBox!!.tag = position
        holder.checkBox!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked.also { mItemChecked[position] = it }) {
                filterCategory.add(data[position].toString())
                dtInterface.setValues(filterCategory)

            } else {
                filterCategory.remove(data[position].toString())
                dtInterface.setValues(filterCategory)
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

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun itemIsChecked(position: Int): Boolean {
        return mItemChecked[position]
    }

    internal class ViewHolder {
        var checkBox: CheckBox? = null
        var mName: TextView? = null
    }

    init {
        this.dtInterface=dtInterface
        this.data = data
        mItemChecked = BooleanArray(data.size)
        for (i in data.indices) {
            mItemChecked[i] = true
            filterCategory.add(data[i].toString())
        }
    }

    interface DataTransferInterface {
        fun setValues(al: MutableList<String>)
    }
}