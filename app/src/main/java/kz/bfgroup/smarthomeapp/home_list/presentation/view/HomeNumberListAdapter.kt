package kz.bfgroup.smarthomeapp.home_list.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData

class HomeNumberListAdapter(
    private val homeNumberClickListener: HomeNumberClickListener
): RecyclerView.Adapter<HomeNumberListViewHolder>() {

    private var dataList: MutableList<StreetApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeNumberListViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.home_number_item, parent, false)

        return HomeNumberListViewHolder(
            rootView,
            homeNumberClickListener
        )
    }

    override fun onBindViewHolder(holder: HomeNumberListViewHolder, position: Int) {
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