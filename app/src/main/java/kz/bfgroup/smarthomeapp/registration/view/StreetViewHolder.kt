package kz.bfgroup.smarthomeapp.registration.view

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData
import kz.bfgroup.smarthomeapp.registration.models.StreetNameApiData

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