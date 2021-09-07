package kz.smart.house.my_requests.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.my_requests.models.MyRequestApiData

class RequestViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val myRequestNumberIdTextView: TextView = itemView.findViewById(R.id.my_request_number_id)
    private val myRequestHeadingTextView: TextView = itemView.findViewById(R.id.my_request_heading)
    private val myRequestTextTextView: TextView = itemView.findViewById(R.id.my_request_text)
    private val myRequestStatusTextView: TextView = itemView.findViewById(R.id.my_request_status_text)
    private val myRequestDateTextView: TextView = itemView.findViewById(R.id.my_request_date)
    private val myRequestAddress: TextView = itemView.findViewById(R.id.my_request_address)


    fun onBind(myRequestApiData: MyRequestApiData) {

        myRequestNumberIdTextView.text = ("№"+myRequestApiData.id)
        myRequestAddress.text = myRequestApiData.address
        myRequestHeadingTextView.text = myRequestApiData.heading
        myRequestTextTextView.text = myRequestApiData.text
//
        when (myRequestApiData.status) {
            "0" -> {
                myRequestStatusTextView.setBackgroundResource(R.drawable.bg_my_request_status_done)
                myRequestStatusTextView.text = "Выполнено"
            }
            "1" -> {
                myRequestStatusTextView.setBackgroundResource(R.drawable.bg_my_request_status_outdated)
                myRequestStatusTextView.text = "Просрочено"
            }
            "2" -> {
                myRequestStatusTextView.setBackgroundResource(R.drawable.bg_my_request_status_new_not_in_process)
                myRequestStatusTextView.text = "Новое/необработано"
            }
            "3" -> {
                myRequestStatusTextView.setBackgroundResource(R.drawable.bg_my_request_status_in_process)
                myRequestStatusTextView.text = "Принято/в работе"
            }
            "4" -> {
                myRequestStatusTextView.setBackgroundResource(R.drawable.bg_my_request_status_new_request)
                myRequestStatusTextView.text = "Новая заявка"
            }
            "5" -> {
                myRequestStatusTextView.setBackgroundResource(R.drawable.bg_my_request_status_cancel)
                myRequestStatusTextView.text = "Отклонена"
            }
        }

        var convertDate = myRequestApiData.data_time?.substring(0,10)
        convertDate = convertDate?.replace("-",".")
        myRequestDateTextView.text = convertDate.toString()
    }
}