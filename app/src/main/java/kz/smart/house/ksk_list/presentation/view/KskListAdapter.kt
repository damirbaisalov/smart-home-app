package kz.smart.house.ksk_list.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.ksk_list.models.KskApiData

class KskListAdapter(
    private val kskItemClickListener: KskItemClickListener
): RecyclerView.Adapter<KskViewHolder>() {

    private var dataList: MutableList<KskApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KskViewHolder {

        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.ksk_item_list, parent, false)

        return KskViewHolder(rootView, kskItemClickListener)
//        return KskViewHolder(
//            View.inflate(parent.context, R.layout.ksk_item_list, null),
//            kskItemClickListener
//        )
    }

    override fun onBindViewHolder(holder: KskViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun setList(kskApiDataList: List<KskApiData>) {
        dataList.clear()
        dataList.addAll(kskApiDataList)
        notifyDataSetChanged()
    }

    fun clearAll() {
        (dataList as? ArrayList<KskApiData>)?.clear()
        notifyDataSetChanged()
    }

    fun filter(name: String) {
        val temp : MutableList<KskApiData> = mutableListOf()
        for (d in dataList) {
            if (d.kskName?.lowercase()?.contains(name)!!) {
                temp.add(d)
            }
        }
        updateList(temp)
    }

    private fun updateList(list : List<KskApiData>) {
        dataList = list as MutableList<KskApiData>
        notifyDataSetChanged()
    }
}