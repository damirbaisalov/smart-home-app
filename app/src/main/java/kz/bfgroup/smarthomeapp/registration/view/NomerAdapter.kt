package kz.bfgroup.smarthomeapp.registration.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.registration.models.NomerNameApiData
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData

class NomerAdapter(
    private val nomerClickListener: NomerClickListener
): RecyclerView.Adapter<NomerViewHolder>() {

    private var dataList: MutableList<NomerNameApiData> = mutableListOf()

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

    fun setList(streetApiDataList: List<NomerNameApiData>) {
        dataList.clear()
        dataList.addAll(streetApiDataList)
        notifyDataSetChanged()
    }

    fun clearAll() {
        (dataList as? ArrayList<NomerNameApiData>)?.clear()
        notifyDataSetChanged()
    }


    fun filter(name: String) {
        val temp : MutableList<NomerNameApiData> = mutableListOf()
        for (d in dataList) {
            val nomerName = d.nomer?.lowercase()
            if (nomerName?.contains(name)!!) {
                temp.add(d)
            }
        }
        updateList(temp)
    }

    private fun updateList(list : List<NomerNameApiData>) {
        dataList = list as MutableList<NomerNameApiData>
        notifyDataSetChanged()
    }
}