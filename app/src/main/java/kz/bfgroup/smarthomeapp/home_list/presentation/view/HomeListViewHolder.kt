package kz.bfgroup.smarthomeapp.home_list.presentation.view

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData
import kz.bfgroup.smarthomeapp.registration.view.StreetClickListener

class HomeListViewHolder(
    itemView: View,
    private val streetClickListener: StreetClickListener
): RecyclerView.ViewHolder(itemView) {

    private val streetItemLayout: ConstraintLayout = itemView.findViewById(R.id.home_item_layout)
    private val streetName : TextView = itemView.findViewById(R.id.home_item_name)

    fun onBind(streetApiData: StreetApiData){

        streetName.text = streetApiData.street
        streetItemLayout.setOnClickListener {
            streetClickListener.onClick(streetApiData.street)
        }
    }
}