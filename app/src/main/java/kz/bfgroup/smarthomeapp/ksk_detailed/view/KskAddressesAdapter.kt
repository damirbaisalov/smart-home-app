package kz.bfgroup.smarthomeapp.ksk_detailed.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData

class KskAddressesAdapter: RecyclerView.Adapter<KskAddressesViewHolder>() {

    private var dataList: MutableList<StreetApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KskAddressesViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.ksk_addresses_item, parent, false)

        return KskAddressesViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: KskAddressesViewHolder, position: Int) {
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
}