package kz.smart.house.registration.view

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.registration.models.StreetApiData

class StreetViewHolder(
    itemView: View,
    private val streetClickListener: StreetClickListener
): RecyclerView.ViewHolder(itemView) {

    private val streetItemLayout: ConstraintLayout = itemView.findViewById(R.id.street_item_constraint_layout)
    private val streetName : TextView = itemView.findViewById(R.id.item_street_name)

    fun onBind(streetApiData: StreetApiData){

//        streetName.text = (streetApiData.street + ", " + streetApiData.nomer)
        streetName.text = streetApiData.street
        streetItemLayout.setOnClickListener {
            streetClickListener.onClick(streetApiData.street)
        }
    }
}