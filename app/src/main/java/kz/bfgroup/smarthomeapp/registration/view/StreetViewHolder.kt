package kz.bfgroup.smarthomeapp.registration.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.smarthomeapp.R
import kz.bfgroup.smarthomeapp.registration.models.StreetApiData

class StreetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val streetName : TextView = itemView.findViewById(R.id.item_street_name)

    fun onBind(streetApiData: StreetApiData){

        streetName.text = (streetApiData.street + ", " + streetApiData.nomer)
    }
}