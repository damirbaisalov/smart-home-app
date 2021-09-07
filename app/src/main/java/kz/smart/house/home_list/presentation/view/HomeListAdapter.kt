package kz.smart.house.home_list.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.registration.models.StreetApiData
import kz.smart.house.registration.view.StreetClickListener

class HomeListAdapter(
    private val streetClickListener: StreetClickListener
): RecyclerView.Adapter<HomeListViewHolder>() {

    private var dataList: MutableList<StreetApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)

        return HomeListViewHolder(
            rootView,
            streetClickListener
        )
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
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
            val street= d.street?.lowercase()
            if (street?.contains(name)!!) {
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