package kz.bfgroup.smarthomeapp.my_requests.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.my_requests.models.MyRequestApiData

class RequestAdapter: RecyclerView.Adapter<RequestViewHolder>() {

    private var dataList: MutableList<MyRequestApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.my_request_item, parent, false)

        return RequestViewHolder(rootView)
//        return KskViewHolder(
//            View.inflate(parent.context, R.layout.ksk_item_list, null),
//            kskItemClickListener
//        )
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun setList(requestApiDataList: List<MyRequestApiData>) {
        dataList.clear()
        dataList.addAll(requestApiDataList)
        notifyDataSetChanged()
    }

    fun clearAll() {
        (dataList as? ArrayList<MyRequestApiData>)?.clear()
        notifyDataSetChanged()
    }
}