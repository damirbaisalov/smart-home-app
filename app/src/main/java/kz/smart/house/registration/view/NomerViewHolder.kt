package kz.smart.house.registration.view

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kz.smart.house.R
import kz.smart.house.registration.models.StreetApiData

class NomerViewHolder(
    itemView: View,
    private val nomerClickListener: NomerClickListener
): RecyclerView.ViewHolder(itemView) {

    private val nomerItemLayout: ConstraintLayout = itemView.findViewById(R.id.nomer_item_constraint_layout)
    private val nomerName : TextView = itemView.findViewById(R.id.item_nomer_name)

    fun onBind(streetApiData: StreetApiData){

//        streetName.text = (streetApiData.street + ", " + streetApiData.nomer)
        nomerName.text = streetApiData.nomer
        nomerItemLayout.setOnClickListener {
            nomerClickListener.onClick(streetApiData.nomer)
        }
    }
}