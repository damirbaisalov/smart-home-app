package kz.bfgroup.smarthomeapp.my_ksk.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.my_ksk.models.CandidatesApiData
import kz.bfgroup.smarthomeapp.news.models.NewsApiData
import kz.bfgroup.smarthomeapp.news.presentation.view.NewsViewHolder

class CandidateAdapter: RecyclerView.Adapter<CandidateViewHolder>() {

    private var dataList: MutableList<CandidatesApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.candidate_item, parent, false)

        return CandidateViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun setList(candidatesApiDataList: List<CandidatesApiData>) {
        dataList.clear()
        dataList.addAll(candidatesApiDataList)
        notifyDataSetChanged()
    }

    fun clearAll() {
        (dataList as? ArrayList<CandidatesApiData>)?.clear()
        notifyDataSetChanged()
    }
}