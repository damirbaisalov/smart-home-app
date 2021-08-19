package kz.bfgroup.smarthomeapp.my_requests.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.my_requests.models.MyRequestApiData

class RequestViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val myRequestNumberIdTextView: TextView = itemView.findViewById(R.id.my_request_number_id)
    private val myRequestHeadingTextView: TextView = itemView.findViewById(R.id.my_request_heading)
    private val myRequestTextTextView: TextView = itemView.findViewById(R.id.my_request_text)
    private val myRequestStatusTextView: TextView = itemView.findViewById(R.id.my_request_status_text)
    private val myRequestDateTextView: TextView = itemView.findViewById(R.id.my_request_date)

    fun onBind(myRequestApiData: MyRequestApiData) {

        myRequestNumberIdTextView.text = ("№"+myRequestApiData.id)
        myRequestHeadingTextView.text = myRequestApiData.heading
        myRequestTextTextView.text = myRequestApiData.text
        if (myRequestApiData.status=="4"){
            myRequestStatusTextView.setBackgroundResource(R.drawable.bg_my_request_status_done)
            myRequestStatusTextView.text = "новая заявка"
        } else {
            myRequestStatusTextView.setBackgroundResource(R.drawable.bg_my_request_status_cancel)
            myRequestStatusTextView.text = "отклонена"
        }
        var convertDate = myRequestApiData.data_time?.substring(0,10)
        convertDate = convertDate?.replace("-",".")
        myRequestDateTextView.text = convertDate.toString()
    }
}