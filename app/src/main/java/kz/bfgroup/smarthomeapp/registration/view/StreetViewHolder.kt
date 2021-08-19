package kz.bfgroup.smarthomeapp.registration.view

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData

class StreetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val streetItemLayout: ConstraintLayout = itemView.findViewById(R.id.street_item_constraint_layout)
    val streetName : TextView = itemView.findViewById(R.id.item_street_name)

    fun onBind(streetApiData: StreetApiData){

        streetName.text = (streetApiData.street + ", " + streetApiData.nomer)
//        streetItemLayout.setOnClickListener {  }
    }
}