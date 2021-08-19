package kz.bfgroup.smarthomeapp.my_ksk.view

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.my_ksk.models.CandidatesApiData

class CandidateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val candidateName: TextView = itemView.findViewById(R.id.candidate_name)
    private val candidateVoteProgress: ProgressBar = itemView.findViewById(R.id.candidate_vote_progress)
    private val candidateVoteNum: TextView = itemView.findViewById(R.id.candidate_vote_num)

    fun onBind(candidatesApiData: CandidatesApiData) {

        candidateName.text = candidatesApiData.fio
        candidateVoteNum.text = (candidatesApiData.golosa + " голосов")

        candidateVoteProgress.max = candidatesApiData.max_golos?.toInt()!!
        candidateVoteProgress.progress = candidatesApiData.golosa?.toInt()!!
    }
}