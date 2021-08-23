package kz.bfgroup.smarthomeapp.news.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.ksk_list.models.KskApiData
import kz.bfgroup.smarthomeapp.ksk_list.presentation.view.KskViewHolder
import kz.bfgroup.smarthomeapp.my_requests.models.MyRequestApiData
import kz.bfgroup.smarthomeapp.news.models.NewsApiData

class NewsAdapter(
    private val newsClickListener: NewsClickListener
): RecyclerView.Adapter<NewsViewHolder>() {

    private var dataList: MutableList<NewsApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)

        return NewsViewHolder(rootView, newsClickListener)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun setList(newsApiDataList: List<NewsApiData>) {
        dataList.clear()
        dataList.addAll(newsApiDataList)
        notifyDataSetChanged()
    }

    fun clearAll() {
        (dataList as? ArrayList<NewsApiData>)?.clear()
        notifyDataSetChanged()
    }

    fun filter(name: String) {
        val temp : MutableList<NewsApiData> = mutableListOf()
        for (d in dataList) {
            if (d.title?.lowercase()?.contains(name)!!) {
                temp.add(d)
            }
        }
        updateList(temp)
    }

    private fun updateList(list : List<NewsApiData>) {
        dataList = list as MutableList<NewsApiData>
        notifyDataSetChanged()
    }
}