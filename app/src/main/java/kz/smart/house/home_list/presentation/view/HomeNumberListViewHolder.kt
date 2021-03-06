package kz.smart.house.home_list.presentation.view

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.registration.models.StreetApiData

class HomeNumberListViewHolder(
    itemView: View,
    private val homeNumberClickListener: HomeNumberClickListener
): RecyclerView.ViewHolder(itemView) {

    private val nomerItemLayout: ConstraintLayout = itemView.findViewById(R.id.home_number_item_layout)
    private val nomerName : TextView = itemView.findViewById(R.id.home_number_item_name)

    fun onBind(streetApiData: StreetApiData){

        nomerName.text = streetApiData.nomer
        nomerItemLayout.setOnClickListener {
            homeNumberClickListener.onClick(streetApiData.id, streetApiData.nomer)
        }
    }
}