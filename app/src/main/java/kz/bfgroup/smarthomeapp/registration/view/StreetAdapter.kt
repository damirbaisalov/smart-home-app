package kz.bfgroup.smarthomeapp.registration.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData

class StreetAdapter(
    private val streetClickListener: StreetClickListener
): RecyclerView.Adapter<StreetViewHolder>() {

    private var dataList: MutableList<StreetApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreetViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.street_item, parent, false)

        return StreetViewHolder(
            rootView,
            streetClickListener
        )
    }

    override fun onBindViewHolder(holder: StreetViewHolder, position: Int) {
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
            val streetPlusNomer = (d.street?.lowercase() + ", " + d.nomer?.lowercase()).lowercase()
            if (streetPlusNomer.contains(name)) {
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