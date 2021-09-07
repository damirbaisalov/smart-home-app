package kz.smart.house.registration.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.registration.models.StreetApiData

class NomerAdapter(
    private val nomerClickListener: NomerClickListener
): RecyclerView.Adapter<NomerViewHolder>() {

    private var dataList: MutableList<StreetApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NomerViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.nomer_item, parent, false)

        return NomerViewHolder(
            rootView,
            nomerClickListener
        )
    }

    override fun onBindViewHolder(holder: NomerViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun setList(streetApiDataList: List<StreetApiData>) {
        dataList.clear()
        dataList.addAll(streetApiDataList)
        notifyDataSetChanged()
    }

    fun clearAll() {
        (dataList as? ArrayList<StreetApiData>)?.clear()
        notifyDataSetChanged()
    }


    fun filter(name: String) {
        val temp : MutableList<StreetApiData> = mutableListOf()
        for (d in dataList) {
            val nomerName = d.nomer?.lowercase()
            if (nomerName?.contains(name)!!) {
                temp.add(d)
            }
        }
        updateList(temp)
    }

    private fun updateList(list : List<StreetApiData>) {
        dataList = list as MutableList<StreetApiData>
        notifyDataSetChanged()
    }
}