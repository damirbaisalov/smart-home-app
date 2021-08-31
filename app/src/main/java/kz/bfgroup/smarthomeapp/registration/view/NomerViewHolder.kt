package kz.bfgroup.smarthomeapp.registration.view

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.registration.models.NomerNameApiData
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData

class NomerViewHolder(
    itemView: View,
    private val nomerClickListener: NomerClickListener
): RecyclerView.ViewHolder(itemView) {

    private val nomerItemLayout: ConstraintLayout = itemView.findViewById(R.id.nomer_item_constraint_layout)
    private val nomerName : TextView = itemView.findViewById(R.id.item_nomer_name)

    fun onBind(nomerNameApiData: NomerNameApiData){

//        streetName.text = (streetApiData.street + ", " + streetApiData.nomer)
        nomerName.text = nomerNameApiData.nomer
        nomerItemLayout.setOnClickListener {
            nomerClickListener.onClick(nomerNameApiData.nomer)
        }
    }
}